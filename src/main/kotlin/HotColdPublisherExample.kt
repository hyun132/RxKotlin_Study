import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Flowables
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {
//    coldPublisher()
//    hotPublisher()
    hotPublisherDisconnect()
}

fun coldPublisher() {
    val coldFlowable = Flowable.interval(1, TimeUnit.SECONDS)

    coldFlowable.subscribe({ println("A : $it") })
    Thread.sleep(2000)
    coldFlowable.subscribe({ println("B : $it") })

    Thread.sleep(3000)
}

fun hotPublisher() {
    val hotFlowable = Flowable.interval(1, TimeUnit.SECONDS).publish()
    hotFlowable.connect()

    hotFlowable.subscribe({ println("A : $it") })
    Thread.sleep(2000)
    hotFlowable.subscribe({ println("B : $it") })

    Thread.sleep(3000)
}

fun hotPublisherDisconnect() {
    val connectableObservable1 = Observable.just(1, 2, 3, 4, 56, 7, 8).doOnNext { Thread.sleep(100) }.publish()
    val connectableObservable2 = Observable.interval(50, TimeUnit.MILLISECONDS).doOnComplete { println("doOncomplete") }.publish()
    connectableObservable2.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribeBy(
        onError = { println("observer1 Error") },
        onNext = { println("observer1 next $it") },
        onComplete = { println("observer1 onComplete") }
    )

    connectableObservable2.connect()
    Thread.sleep(100)
    val disposable2 = connectableObservable2.doOnDispose { println("connectableObservable2 is disposed") }.observeOn(Schedulers.computation()).subscribeBy(
        onError = { println("observer2 Error") },
        onNext = { println("observer2 next $it") },
        onComplete = { println("observer2 onComplete") }
    )

    Thread.sleep(1000)
    disposable2.dispose()
    Thread.sleep(1000)


//
//    connectableObservable2.subscribeOn(Schedulers.computation()).subscribeBy(
//        onError = { println("observer1 Error") },
//        onNext = { println("observer1 next $it") },
//        onComplete = { println("observer1 onComplete") }
//    )
//    val disposable2 = connectableObservable2.subscribeOn(Schedulers.computation()).subscribeBy(
//        onError = { println("observer2 Error") },
//        onNext = { println("observer2 next $it") },
//        onComplete = { println("observer2 onComplete") }
//    )
//
//    Thread.sleep(3000)
//    connectableObservable2.connect()
    Thread.sleep(1000)
    disposable2.dispose()
    Thread.sleep(1000)



}
