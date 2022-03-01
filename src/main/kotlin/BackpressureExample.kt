import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableSubscriber
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.ResourceSubscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

fun main() {
//    setBufferSize1()
//    setBufferSize2()
//    setBufferSize2Observable()
//    setbufferSize5Flowable()

//    backpressureExampleFlowableWithBackpressureStrategy()
//    backpressureExampleFlowableWithBackpressureStrategyDrop()
    backpressureExampleFlowableWithBackpressureStrategyDrop2()
//    backpressureExampleFlowable()
//    backpressureExampleObservable()
}

fun setBufferSize1() {
    val flowable = Flowable.interval(300, TimeUnit.MILLISECONDS)
        .onBackpressureDrop()

    flowable.observeOn(Schedulers.computation(), false, 1)
        .subscribe(object : ResourceSubscriber<Long>() {
            override fun onNext(t: Long?) {
                Thread.sleep(1000)
                println("$t : ${Thread.currentThread().name}")
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

fun backpressureExampleObservable() {
    Observable.range(1, 10000)
        .doOnNext { println(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread(), false, 16)
        .subscribe {
            Thread.sleep(10)
            println("observable : $it")
        }
    Thread.sleep(5000)
}

fun backpressureExampleFlowable() {
    Flowable.range(1, 10000)
        .doOnNext { println(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread(), false, 16)
        .subscribe {
            Thread.sleep(10)
            println("flowable : $it")
        }
    Thread.sleep(5000)
}

/**
 * 해야할 일 배압관련 전략 예제 다 실행해보고
 *  maybe와 single 예제 실행하고 공부하기
 *  그리고 데이터어쩌고 책 그 부분 공부할것
 *  배고프니까 집에가자
 */
fun backpressureExampleFlowableWithBackpressureStrategy() {
    Flowable.range(1, 10000)
        .doOnNext { println(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .subscribeBy(
            onNext = {
                Thread.sleep(10)
                println("flowable : $it")
            },
            onError = { println(it.message) },
            onComplete = { println("onComplete")}
        )
    Thread.sleep(5000)
}

fun backpressureExampleFlowableWithBackpressureStrategyDrop() {
//1 2 3/ 4 5 6 7 8 9
    Flowable.create<Int>({
        for (i in 1..200){
            it.onNext(i)
            Thread.sleep(1)
        }
    },BackpressureStrategy.MISSING)  // flowable에서 제공하는 backpressue를 사용하지 않겠다.
        .onBackpressureDrop { println("drop $it") }
        .observeOn(Schedulers.computation())
        .subscribe(object :FlowableSubscriber<Int>{
            lateinit var subscription: Subscription
            override fun onSubscribe(s: Subscription) {
                println("onSubscribe")
                subscription = s
                s.request(3)
            }

            override fun onNext(t: Int?) {
                Thread.sleep(3)
                println(t)
                subscription.request(3)
            }

            override fun onError(t: Throwable?) {
                println(t?.message)
            }

            override fun onComplete() {
                println("onComplete")
            }

        })

    Thread.sleep(1000)

}

fun backpressureExampleFlowableWithBackpressureStrategyDrop2(){
    Flowable.range(1,200)
        .onBackpressureDrop { println("drop $it") }
        .observeOn(Schedulers.computation(),false,16)
        .subscribe(object :FlowableSubscriber<Int>{
            lateinit var subscription: Subscription
            override fun onSubscribe(s: Subscription) {
                println("onSubscribe")
                subscription = s
                s.request(3)
            }

            override fun onNext(t: Int?) {
//                Thread.sleep(3)
                println(t)
                subscription.request(3)
            }

            override fun onError(t: Throwable?) {
                println(t?.message)
            }

            override fun onComplete() {
                println("onComplete")
            }

        })

    Thread.sleep(1000)
}

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
