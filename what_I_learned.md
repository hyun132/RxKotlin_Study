## RxKotlin

### Reactive Programming? 
+ 데이터가 통지(발행)될 때 마다 관련 프로그램이 반응해 데이터를 처리하는 프로그래밍 방식!

### Rx?
+ 관찰 가능한 시퀀스를 사용하여 이벤트 기반 비동기 프로그램을 구성하기 위한 라이브러리
    > Rx 는 Observer패턴, Iterator패턴, 함수형 프로그래밍의 조합이다.
    - 옵저버 패턴은 객체의 상태를 관찰하는 관찰자들을 객체에 등록하여 상태변화가 있을 때마다 객체가 관찰자들에게 변경을 통지하는 디자인 패턴
    - 이터레이터 패턴은 컬렉션의 구현 방법을 노출시키지 않고 그 안에 존재하는 모든 항목에 접근할 수 있도록 하는 패턴(next()를 통해서 모든 항목에 접근)
    - 함수형 프로그래밍은 "대입문이 없는 프로그래밍"이다. 즉 side effect가 없는 순수 함수를 사용하며 참조 투명성을 가진다.(외부의 값을 변경하지 않고, 동일한 인자에 대해 항상 동일한 결과를 반환해야 한다.)
        ```
        fun doNotLikeThis() {
            var operaterState = OperaterState.ADD
            val flowable = Flowable
                .interval(300L, TimeUnit.MILLISECONDS)
                .take(6)
                .scan { sum, data ->
                    if (operaterState == OperaterState.ADD) {
                        return@scan sum + data
                    } else return@scan sum * data
                }
       
            flowable.subscribe(object :Subscriber<Long>{
                lateinit var subscription:Subscription
                override fun onSubscribe(s: Subscription?) {
                    if (s != null) {
                        subscription = s
                    }
                    s?.request(1)
                }
        
                override fun onNext(t: Long?) {
                    println("current value is $t")
                    subscription.request(1)
                }
        
                override fun onError(t: Throwable?) {
                    println("onError ${t?.message}")
                }
        
                override fun onComplete() {
        
                }
            })
        
            Thread.sleep(1000)
            println("계산 방법 변경!")
            operaterState = OperaterState.MULTIPLY
        
            Thread.sleep(2000)
        }
      ---- 결과 ---
      current value is 0
      current value is 1
      current value is 3
      계산 방법 변경!
      current value is 9
      current value is 36
      current value is 180
        ```
    https://mangkyu.tistory.com/111
- 쉽게 dataStream을 만들고 operator를 붙이고 구독할 수 있다.


### Reactive Stream?
- 라이브러리나 프레임워크에 상관없이 데이터 스트림을 비동기로 다룰 수 있는 메커니즘으로, 편리하게 사용할 수 있는 인터페이스를 제공한다.
    > dataStream은 이미 생성된 데이터의 집합과는 다르게 앞으로 발생할 가능성이 있는 데이터도 포함한다.

+ reactive stream은 크게 세가지로 구성되어있다.
    + 데이터를 생산하는 Publisher
    + 생산된 데이터를 받아 처리하는 Subscriber
    + Publisher와 Subscriber의 기능이 모두 있는 Processor
+ Subscriber가 Publisher를 구독해 publisher가 발행하는 이벤트나 데이터를 전달받는다.

#### java's Stream vs Reactive Stream
- 이 둘의 차이점은 무엇일까?
    > Stream은 시간이 지남에 따라 제공되는 데이터 요소의 시퀀스

        - Java Streams : 풀 기반 동기 (Java 스트림은 한 번만 사용할 수 있으며 풀 기반)
        - reactive Stream : push 기반 비동기식 (데이터 생산자가 데이터 소비자(가입자)가 데이터를 받는 시기를 결정한다) 

    즉 데이터를 소비자가 직접 가져오느냐, 생산자가 전달해주느냐에 차이가 있다.
#### Reactive Stream에는 기본 규칙이 있다.
    - 구독 시작 통지는 한 구독당 한 번만 발생한다.
    - 통지는 순차적으로 이루어진다.
      (여러 통지를 동시에 할 수 없다.데이터가 동시에 통지돼 불일치가 발생하는 것을 방지하기 위해??자세히 설명 추가)
    - null을 통지하지 않는다.
    - publisher의 처리는 완료 또는 에러를 통지해 종료한다.
      (완료/에러 발생 이후에는 더 이상이 데이터 통지나 완료/에러 통지가 발생하지 않는다)

###RxJava
    RxJava의 원리와 구조를 알아보자
#### RxJava의 특징
  - 앞서 언급한 바와 같이 RxJava는 소비자가 생산자를 구독하는 형태이다.
  즉, 데이터를 생성하는 측과 소비하는 측을 나눌 수 있기 때문에 쉽게 데이터 스트림을 처리할 수 있다
  - 데이터 전달의 완료와 에러를 통지할 수 있어 데이터 발행이 끝나거나 에러가 발생하는 시점에 쉽게 대응할 수 있다.
  - 쉽게 비동기 처리를 할 수 있다.
  
  이와 같은 구조를 가진다.
  ```
  source.operator1().operator2().operator3().subscribe(consumer);
  ```
#### RxJava의 구성
- RxJava는 Reactive Stream을 기반으로 구현되었기 때문에 앞서 언급한 바와 같이 observer 패턴과 iterator패턴의 특징을 가진다.
- RxJava는 비동기 처리를 위한 Api를 제공하기 때문에, 생산자와 소비자의 작업을 쉽게 비동기 처리할 수 있다.
- 에러 발생시 대응하는 방법을 제공한다.
- 리소스 관리 방법을 제공한다.
- 배압 제어 기능을 제공한다.
  > 데이터 통지량을 제어하는 기능
##### RxJava의 생산자와 소비자
- RxJava에는 **Cold**생산자와 **Hot**생산자가 있다.
- Cold 생산자는 소비자가 생산자를 구독할 때 마다 새로운 stream이 생성된다.
    ```
    fun coldPublisher(){
        val coldFlowable = Flowable.interval(1, TimeUnit.SECONDS)
    
        coldFlowable.subscribe({ println("A : $it") })
        Thread.sleep(2000)
        coldFlowable.subscribe({ println("B : $it") })
    
    ...
    A : 0
    A : 1
    A : 2
    B : 0
    A : 3
    B : 1
    A : 4
    B : 2
    ```
- Hot 생산자는 구독자의 유무 상관없이 데이터 생성되는 시점에 Data를 발행한다. 그래서 구독자들은 구독 후 발행되는 데이터를 받을 수 있다. 즉 구독 이전에 발행된 데이터를 받을 수 없다.
    ```
    fun hotPublisher(){
        val hotFlowable = Flowable.interval(1, TimeUnit.SECONDS).publish()  //publish()를 통해 cold에서 hot으로 변환
        hotFlowable.connect()
    
        hotFlowable.subscribe({ println("A : $it") })
        Thread.sleep(2000)
        hotFlowable.subscribe({ println("B : $it") })
    
        Thread.sleep(3000)
    }
    
    A : 0
    A : 1
    A : 2
    B : 2
    A : 3
    B : 3
    A : 4
    B : 4
    ```

- 생산자로는 Flowable, Observable, Single, Maybe, Completable이 있다. 
- 소비자는 Subscriber과 Observable이 있다.
- 그리고 생산자와 소비자의 특성을 모두 가진 Subject가 있다.
Reactive Stream을 구현한 Publisher/Subscriber과 그렇지 않은 Observable/Observer가 있다.

#### Flowable vs Observable
이들은 데이터를 생성하고 통지하는 클래스이지만 Flowable은 배압 기능이 있고, Observable은 없다.
Flowable은 Subscription을 사용하여 통지하는 데이터의 개수를 제어하고 구독을 해지할 수 있다.
Observable은 배압기능이 없기 때문에 데이터 개수를 요청하지 않으며 Disposable 인터페이스를 사용하여 구독해지를 할 수 있다.  
> 배압이란, 데이터 생산과 소비가 불균형적일 때 일어나는 현상이다. 무한개의 데이터를 1초마다 발행하고, 10초마다 소비 한다면, 소비되지 않은(소비 될) 데이터는 스트림에 계속 쌓이게 된다. 이로인해 OutOfMemoryError가 발생할 수 있다. 이러한 현상을 배압(Backpressure)이라고 하며 RxJava에서는 Subscription을 이용해 이를 제어한다.
  
  ##### Flowable
  일반적으로 Observable이 Flowable보다 오버헤드가 적다고 알려져 있다. (어떤 처리를 하기 위해 들어가는 간접적인 처리 시간 · 메모리)
https://github.com/ReactiveX/RxJava/wiki/Backpressure#reactive-pull-backpressure-isnt-magic
   따라서 일반적으로 성능이 중요하다면 Observable을 사용하는것이 좋다고 한다.
        
  이 외에 OOM이나 MissingBackpressureException을 피하는 방법으로 Subscription을 사용해 
  
  ##### Observable
- Observable의 동작을 설명하기 위해서는 크게 세 가지 요소가 필요합니다.
  Observable(데이터 생성자), Observer(구독자), Disposable(구독 제어).
- Observable은 각 rx체인의 생성 블록으로 subscribe()의 파라미터로 Observer를 전달해 구독하게 됩니다.
  
- Observer는 onSubscribe(), onNext(), onComplete()를 가지고 있는 인터페이스로 Observer가 Observable을 구독하면 Disposable객체를 생성하고 Observer에게 이 객체를 콜백을 통해 전달한다.
  이렇게 구독을 하게 되면 Observable은 onNext()를 통해 Observer에게 값을 전달하고, Disposable을 통해 dispose()할 때 까지 값을 전달한다.
  ![image](https://user-images.githubusercontent.com/46836642/155872730-347c61f1-fb62-4d3b-949a-178db8cebc76.png)
  
##### Flowable
- Flowable은 동작을 설명하기 위해서는 크게 세 가지 요소가 필요합니다.
  Flowable은(데이터 생성자), Subscriber(구독자), Subscription를(구독 제어 및 값 요청).
- Flowable은 Observable과 Observer은 Subscriber과 유사하다.
  Flowable은 Disposable 대신 Subscription를 가지는데 subscription은 request(n) 메서드를 가진다.
- Subscriber는 Flowable에게 필요한 개수만큼의 데이터를 요청하고 그 요청을 받아야 Flowable이 값을 발행한다.
  ![image](https://user-images.githubusercontent.com/46836642/155872875-ad5b2e0e-c730-4371-89fd-f19bb273216f.png)
  
- 하지만 별도의 설정이 없는 경우 무한으로 데이터를 받도록 설정되어있다.
    ```
    public final Disposable subscribe(@NonNull Consumer<? super T> onNext, @NonNull Consumer<? super Throwable> onError,
            @NonNull Action onComplete) {
        Objects.requireNonNull(onNext, "onNext is null");
        Objects.requireNonNull(onError, "onError is null");
        Objects.requireNonNull(onComplete, "onComplete is null");
    
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(onNext, onError, onComplete, FlowableInternalHelper.RequestMax.INSTANCE);
    
        subscribe(ls);
    
        return ls;
    }
    ```
  
  ###### Flowable
    - 대량의 데이터(10000개 이상) 처리할 때
    - 네트워크 통신이나 데이터베이스등 I/O처리할 때
  ###### Observable
    - GUI 이벤트
    - 소량의 데이터
    - 동기방식일 때
  

#### CompositeDisposable
가지고 있는 모든 disposable들의 dispose()를 호출할 수 있는 클래스로, 한번에 여러 구독을 취소 할 수 있다.
```
/**
 * CompositeDisposable은 여러 Disposable을 모아 복수의 구독을 동시에 해지할 수 있다.
 */
fun main() {
    val compositeDisposable = CompositeDisposable()

    val flowableA = Flowable.range(1,10)
        .doOnCancel { println("flowable A is canceled") }
        .observeOn(Schedulers.computation())
        .subscribe{
            Thread.sleep(10)
            println("A's data is $it")}

    val flowableB = Flowable.range(1,10)
        .doOnCancel { println("flowable B is canceled") }
        .observeOn(Schedulers.computation())
        .subscribe{
            Thread.sleep(10)
            println("B's data is $it")}

    compositeDisposable.addAll(flowableA,flowableB)
    Thread.sleep(50)
    compositeDisposable.dispose()
}
```

### RxJava에서의 비동기

- RxJava는 구조상 데이터를 통지하는 측과 받는 측의 작업이 논리적으로 분리되어 있어 각각 다른 스레드에서 실행하기 쉽다.


#### Scheduler
- RxJava에서 제공하는 스레드를 관리하는 클래스.
- 이 스케쥴러를 이용해서 어떤 작업을 어떤 쓰레드에서 수행할지를 제어할 수 있다.

##### subscribeOn()
- 생산자의 처리 작업을 어떤 스케줄러에서 실행할지 설정하는 메서드.
- subscribeOn()는 생산자가 처리 작업을 할 스케줄러를 설정해주는데, 최초 1번만 설정할 수 있다. 여러번 설정할 경우 처음 설정 이후의 스케줄러는 무시한다. (interval()등으로 생성한 생산자처럼 스케줄러가 자동으로 지정되는 경우에도 개발자가 나중에 subscribeOn()으로 다른 스케줄러를 지정해도 반영되지 않는다.)
    ```
    fun setOnlyTheFirstScheduler() {
        Flowable.just(1,2,3,4,5)
            .subscribeOn(Schedulers.computation())
            .subscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.single())
            .subscribe{ println("$it on ${Thread.currentThread().name}")}
    
        Thread.sleep(100)
    }
    ---- 결과 ---
    1 on RxComputationThreadPool-1
    2 on RxComputationThreadPool-1
    3 on RxComputationThreadPool-1
    4 on RxComputationThreadPool-1
    5 on RxComputationThreadPool-1
    ```

- 주의할 점은 **io()** 로 가져온 스케줄러는 스레드풀에 남은 스레드가 없을 때 새로운 스레드를 만든다.(I/O작업은 일반적으로 대기시간이 발생할 가능성이 커서 효율적으로 처리 작업을 할 수 있기 때문)
하지만 **computation()** 으로 가져온 스케줄러는 논리 프로세서 수를 넘지 않는 범위에서 스레드 주고받음.(연산처리 작업은 대기가 발생하지 않아 논리프로세서수를 초과할 경우 실행 스레드를 전환하는 일이 발생하여 오히려 성능이 저하될 수 있기 때문)

- Flowable/Observable을 생성하는 메서드에 따라 데이터가 발행될 스레드가 달라진다.
기본적으로 **just**나 **from**처럼 이미 생성된 데이터를 통지하는 생산자는 main 스레드에서 작업을 수행하며, **timer**나 **interval**처럼 시간과 관련된 처리 작업은 다른 스레드에서 작동한다.
    ```
    fun justExample(){
        /**
         * just, from처럼 미리 생성된 데이터 통지하는 생산자는 메인 스레드에서 동작
         */
        Flowable.just(1,2,3)
            .doOnNext{ Thread.sleep(1000L);println("emit $it ${System.currentTimeMillis()} in ${Thread.currentThread().name}")}
            .subscribe{Thread.sleep(500L); println("in ${Thread.currentThread().name}")}
    
        println("end")
    }
    ---- 결과 ---
    emit 1 1645881629603 in main
    in main
    emit 2 1645881631121 in main
    in main
    emit 3 1645881632640 in main
    in main
    end
    ```
    
    ```
    fun intervalExample(){
        /**
         * 다른 스레드에서 처리 작업
         */
        Flowable.interval(1000L,TimeUnit.MILLISECONDS)
            .doOnNext{ println("emit $it ${System.currentTimeMillis()} in ${Thread.currentThread().name}")}
            .take(3)
            .subscribe{Thread.sleep(500L); println("in ${Thread.currentThread().name}")}
    
        println("end")
    }
    ---- 결과 ---
    end
    emit 0 1645881625585 in RxComputationThreadPool-1
    in RxComputationThreadPool-1
    emit 1 1645881626594 in RxComputationThreadPool-1
    in RxComputationThreadPool-1
    emit 2 1645881627581 in RxComputationThreadPool-1
    in RxComputationThreadPool-1
    ```
  
  interval 내부를 보면
  ```
    public static Flowable<Long> interval(long period, @NonNull TimeUnit unit) {
            return interval(period, period, unit, Schedulers.computation());
    }
    ```
  이렇게 기본으로 설정되어 있는것을 확인할 수 있다.

##### observeOn()
- 데이터를 소비하는 작업을 어떤 스케줄러에서 실행할지 설정하는 메서드.
- observeOn()으로 스케줄러를 지정하면 이후 연산자들은 지정된 스케줄러에서 작업을 수행한다.
  subscribeOn()과는 다르게 연산자마다 서로 다른 스케줄러를 지정할 수 있는데 이는 연산자가 새로운 observable/flowable을 반환하기 때문이다.
      
    ```
      fun chainingExample() {
          Flowable.range(1, 50)
              .doOnNext { println("emit $it : ${Thread.currentThread().name}") }
              .subscribeOn(Schedulers.trampoline())
              .observeOn(Schedulers.computation())
              .filter {
                  println("filter $it : ${Thread.currentThread().name}")
                  it % 2 == 0
              }
              .observeOn(Schedulers.io())
              .map {
                  println("map $it : ${Thread.currentThread().name}")
                  it*it
              }
              .take(10)
              .observeOn(Schedulers.io())
              .subscribe { println("take emitted $it at ${Thread.currentThread().name}") }
      
          Thread.sleep(1000)
      }
    
    ...
    emit 49 : main
    filter 44 : RxComputationThreadPool-1
    emit 50 : main
    filter 45 : RxComputationThreadPool-1
    ...
    filter 50 : RxComputationThreadPool-1
    map 4 : RxCachedThreadScheduler-2
    map 6 : RxCachedThreadScheduler-2
    take emitted 4 at RxCachedThreadScheduler-1
    ...
    ```
    이처럼 각 연산이 다른 스케줄러에서 실행됨을 알 수 있다.
    위에서 사용한 연산들을 보면
    ```
    public final <@NonNull R> Flowable<R> map(@NonNull Function<? super T, ? extends R> mapper) {
    public final Flowable<T> filter(@NonNull Predicate<? super T> predicate) {
    public final Flowable<T> take(long count) {
    ```
    이와같이 Flowable 타입을 반환하는 것을 알 수 있다.
    즉 연산의 결과가 Flowable이므로 이 결과를 다시 연산하고 구독할 수 있다 => chaining

- observeOn()을 통해 배압을 적용할 수 있다.

    ```public final Flowable<T> observeOn(@NonNull Scheduler scheduler, boolean delayError, int bufferSize) {```
    
    observeOn의 파라미터를 통해 buffer의 사이즈를 설정하면, 소비자가 데이터를 처리할 수 있을 때 request를 통해 데이터를 요청한 만큼(buffer의 사이즈)의 데이터를 전달한다.
    ![image](https://user-images.githubusercontent.com/46836642/155868978-7fc7b536-ea25-4119-ac80-00d205b7a6f9.png)
    ```
    fun setBufferSize2() {
        val flowable = Flowable.interval(200, TimeUnit.MILLISECONDS)
            .doOnNext { println("emit $it") }
            .onBackpressureDrop()
    
        flowable.observeOn(Schedulers.computation(), false, 2)
            .subscribe(object : ResourceSubscriber<Long>() {
                override fun onNext(t: Long?) {
                    println("$t : ${Thread.currentThread().name}")
                    Thread.sleep(450)
                }
    
                override fun onError(t: Throwable?) {
                    TODO("Not yet implemented")
                }
    
                override fun onComplete() {
                    TODO("Not yet implemented")
                }
    
            }
            )
    
        Thread.sleep(5000)
    }
    
    emit 0
    0 : RxComputationThreadPool-1
    emit 1
    emit 2
    1 : RxComputationThreadPool-1
    emit 3
    emit 4
    emit 5
    5 : RxComputationThreadPool-1
    emit 6
    emit 7
    6 : RxComputationThreadPool-1
    ...
    ```
