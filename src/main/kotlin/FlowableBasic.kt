import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.kotlin.toFlowable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

fun main() {
//    doNotLikeThis()
    basicSample()
}

enum class OperaterState {
    ADD,
    MULTIPLY
}

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

//    flowable.subscribe({ t -> println("current value is $t") })
    flowable.subscribe(object : Subscriber<Long> {
        lateinit var subscription: Subscription
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
            println("onComplete")
        }
    })

    Thread.sleep(1000)
    println("계산 방법 변경!")
    operaterState = OperaterState.MULTIPLY

    Thread.sleep(2000)
}

fun createFlowabelWithfromIterable() {
    val list = listOf<Int>(1, 2, 3)
    val flowable = Flowable.fromIterable(list)
    flowable.subscribe { println(it) }
    list.toFlowable().subscribe{ println(it)}
}

fun basicSample(){
    Flowable
        .interval(30L, TimeUnit.MILLISECONDS)
        .take(6)
        .subscribe({println(it)})

    Thread.sleep(500)
}
