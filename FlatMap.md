### Map

- map은 단일 스트림의 원소를 매핑 시킨 후 매핑 시킨 값을 다시 스트림으로 반환하는 중간 연산
- `Function<Int, String> getMap = item -> "<"+item+">";` 과 같은 T -> R

```
Observable.fromArray("aaa", "bbb", "ccc", "ddd")
        .map { it[0].code }
        .subscribe { println(it) }

-----
97
98
99
100
```

### 😡 FlatMap

- flatMap은 하나의 원소를 받아서 여러개의 변형된 아이템을 발행 가능.
- `Function<Int, Publisher<String>> getMap = item -> Observable.just(item+"1",item+"2"...)` 과 같은 T → Publisher<R>
- 실제로 map()을 먼저 한 다음 -> merge로 감싸주어 Observable로 반환한다고 하는데 확인해보자(merge는 observable의 발행 속도에 따라 데이터 방출. 병합순서에 따른 발행순서 보장되지 않음. 그냥 빨리 방출된게 먼저)
- 아래 예는 flatmap을 이용해 구구단을 출력하는 코드이다.

```
Observable.range(1,9)
        .flatMap { row ->
            Observable.range(1,9).map {
                print("$row * $it = ")
                it*row
            }
        }.subscribe { println(it) }
```

- 만약 이렇게 비동기로 처리 할 경우, 값의 순서가 보장되지 않는다

```
Observable.range(1,9)
        .flatMap { row ->
            Observable.range(1,9).map {
                print("$row * $it = ")
                it*row
            }.observeOn(Schedulers.io())
        }.subscribe { println(it) }
```

flatMap은 보면 하나의 값을 받았을 때 내부에서 하나 이상의 값을 방출하는 observable(publisher)을 생성한다.

flatMap 내부를 보면 `MergeSubscriber` 라는 클래스를 가지고 있으며, 그 내부에서는 innersubscriber를 만들어서 각 생성되는 observable을 구독하고 거기서 방출되는 값을 downstream으로 방출한다.

아래가 `FlowableFlatMap`클래스 내의 `MergeSubscriber`클래스의 생성자이다.

```
MergeSubscriber(Subscriber<? super U> actual, Function<? super T, ? extends Publisher<? extends U>> mapper,
        boolean delayErrors, int maxConcurrency, int bufferSize) {
    this.downstream = actual;
    this.mapper = mapper;
    this.delayErrors = delayErrors;
    this.maxConcurrency = maxConcurrency;
    this.bufferSize = bufferSize;
    this.scalarLimit = Math.max(1, maxConcurrency >> 1);
    subscribers.lazySet(EMPTY);
}
```

upstream에서 onNext()를 통해 값이 전달되면, mergeSubscirber는 그 값을 받아 mapper에 전달한다.  그 결과 `publisher` 를 생성하고(아래 코드에서는 p)

    ```
    public void onNext(T t) {
        // safeguard against misbehaving sources
        if (done) {
            return;
        }
        Publisher<? extends U> p;
        try {
            p = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Publisher");
    ```

새로운 `InnerSubscriber`를 생성해서 방금 만든 publisher(upstream의 값을 받아와서 만든 새로운 publisher)를 구독하도록 한다.

```
InnerSubscriber<T, U> inner = new InnerSubscriber<>(this, bufferSize, uniqueId++);
if (addInner(inner)) {
    p.subscribe(inner);
}
```

`innerSubscriber`가 `innerPublisher`(위에서 생성된 publisher)에서 값을 받아오면, 그 값을 `tryEmit()`을 통해`drainLoop()`를 통해 값을 가져와 child에게 전달한다.

```
@Override
public void onNext(U t) {
    if (fusionMode != QueueSubscription.ASYNC) {
        parent.tryEmit(t, this);
```

### Merge

- 값의 **가공 없이** 다수의 Observable을 합쳐서 출력한다.
- Observable의 발생 속도에 따라 방출하기 때문에 병합순서와 같은 발행순서를 보장하지 않음.

### Concat

- ‘사슬로 잇듯이’ observable들을 이어주는 함수.
- `concat(observable1, observable2)` 를 예로 들면, `observable1`이 `complete`되어야 다음 `observable2`가 수행된다.

### Zip

- 두가지 observable을 하나로 결합할 때 사용.
- Observable.zip(옵져버블1, 옵져버블2, (옵1의 원소, 옵2의 원소-> 처리)).subscribe
- zip으로 묶었을 때 한 옵저버블의 발행이 늦어질 때 그 원소가 나올때 까지 다운스트림으로의 발행을 기다림.
- 데이터 발행은 짧은쪽

```
val observable1 = Observable.just('a', 'b', 'c', 'd', 'e', 'f').doOnNext { Thread.sleep(50);println("emit1 $it") }
        .subscribeOn(Schedulers.io()).doOnComplete { println("Complete") }
    val observable2 =
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).doOnNext { println("emit2 $it") }.subscribeOn(Schedulers.io())
            .doOnComplete { println("Complete") }
    Observable.zip(observable1, observable2, { a, b -> "$a$b" }).subscribeBy(
        onNext = { println(it) }, onComplete = { println("subscriber complete") }
    )
-------
emit2 8
emit2 9
Complete    ---> observable2종료
emit1 a
a1
emit1 b
...
e5
emit1 f
f6
Complete    --> observable1 종료
subscriber complete  --> subscribe 종료
```


**FlatMap & SwitchMap  & ConcatMap 의 비교**

FlatMap : 변환 연산자 - 아이템의 순서를 보장하지 않고, 비동기적으로 동작한다.(빠르다)

SwitchMap : 조합 연산자 - 이전의 observable은 구독하지 않고 늘 최신만 구독한다.⇒ 모든 데이터를 받아오지 않음.

ConcatMap : 계산 및 집합 연산자 - 순서를 보장한다. 근데 동기적으로 동작한다.(느리다)
