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

#### Reactive Stream에는 기본 규칙이 있다.
    - 구독 시작 통지는 한 구독당 한 번만 발생한다.
    - 통지는 순차적으로 이루어진다.(여러 통지를 동시에 할 수 없다.데이터가 동시에 통지돼 불일치가 발생하는 것을 방지하기 위해)
    - null을 통지하지 않는다.
    - publisher의 처리는 완료 또는 에러를 통지해 종료한다.


###RxJava
#### RxJava의 특징
  - 앞서 언급한 바와 같이 RxJava는 소비자가 생산자를 구독하는 형태이다.
  즉, 데이터를 생성하는 측과 소비하는 측을 나눌 수 있기 때문에 쉽게 데이터 스트림을 처리할 수 있다
  - 데이터 전달의 완료와 에러를 통지할 수 있어 데이터 발행이 끝나거나 에러가 발생하는 시점에 쉽게 대응할 수 있다.
  - 쉽게 비동기 처리를 할 수 있다.
#### RxJava의 구성
##### RxJava의 생산자
- Flowable, Observable, Single, Maybe, Completable이 있다.
Reactive Stream을 구현한 Publisher/Subscriber과 그렇지 않은 Observable/Observer가 있다.
이와 같은 구조를 가진다.
```
source.operator1().operator2().operator3().subscribe(consumer);
```
#### Flowable vs Observable
이들은 데이터를 생성하고 통지하는 클래스이지만 Flowable은 배압 기능이 있고, Observable은 없다.
Flowable은 Subscription을 사용하여 통지하는 데이터의 개수를 제어하고 구독을 해지할 수 있다.
Observable은 배압기능이 없기 때문에 데이터 개수를 요청하지 않으며 Disposable 인터페이스를 사용하여 구독해지를 할 수 있다.  
> 배압이란, 데이터 생산과 소비가 불균형적일 때 일어나는 현상이다. 무한개의 데이터를 1초마다 발행하고, 10초마다 소비 한다면, 소비되지 않은(소비 될) 데이터는 스트림에 계속 쌓이게 된다. 이로인해 OutOfMemoryError가 발생할 수 있다. 이러한 현상을 배압(Backpressure)이라고 하며 RxJava에서는 Subscription을 이용해 이를 제어한다.
  
  ##### Flowable
  - 
  일반적으로 Observable이 Flowable보다 오버헤드가 적다고 알려져 있다.
  따라서 일반적으로 성능이 중요하다면 Observable을 사용하는것이 좋다고 한다.
        
  이 외에 OOM이나 MissingBackpressureException을 피하는 방법으로 Subscription을 사용해 
  ###### Flowable
    - 대량의 데이터(10000개 이상) 처리할 때
    - 네트워크 통신이나 데이터베이스등 I/O처리할 때
  ###### Observable
    - GUI 이벤트
    - 소량의 데이터
    - 동기방식일 때
    
##### Flowable/Observable을 생성하는 메서드에 따라 데이터가 발행될 스레드가 달라진다.
기본적으로 just나 form처럼 이미 생성된 데이터를 통지하는 생산자는 main 스레드에서 작업을 수행하며, timer나 interval처럼 시간과 관련된 처리 작업은 다른 스레드에서 작동한다.
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
