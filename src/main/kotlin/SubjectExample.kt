import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

val printObserver = object : Observer<String> {
    override fun onSubscribe(d: Disposable) {
        println("onSubscribe")
    }

    override fun onNext(t: String) {
        println("get: $t")
    }

    override fun onError(e: Throwable) {
        println("onError: ${e.message}")
    }

    override fun onComplete() {
        println("onComplete")
    }
}

fun main() {
//    publishSubjectExample()
//    publishSubjectExample2()
    publishSubjectExample3()
}


/**
 * PublishSubject 얘는 HotObservable. 구독 시점부터 데이터 발행
 */
fun publishSubjectExample() {
    val source = Observable.interval(1, TimeUnit.SECONDS)
    val source2 = Observable.interval(500, TimeUnit.MILLISECONDS)

    val subject = PublishSubject.create<String>()

    source.map { "source : $it" }.subscribe(subject)
    source2.map { "source2 : $it" }.subscribe(subject)

    subject.subscribe { println("subscriber : $it") }





    Thread.sleep(3000)
}

/**
 * subject가 먼저 observer에게 구독되고
 * subject가 source를 구독하면
 *
 * source에서 방출된 값이 subject -> obserer로 전달된다.
 */
fun publishSubjectExample2() {
    val source3 = Observable.just(1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009)
    val source4 = Observable.interval(50, TimeUnit.MILLISECONDS)
    val subject = PublishSubject.create<String>()

    subject.observeOn(Schedulers.newThread()).subscribe(printObserver)

    source4.map { "source4 : $it" }.subscribe(subject)
    source3.map { Thread.sleep(100); "source3 : $it" }.subscribe(subject)

    Thread.sleep(2000)
}

fun publishSubjectExample3() {
    val source3 = Observable.just(1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009)
        .doOnNext { println("source3 emit:$it") }
    val source4 = Observable.interval(50, TimeUnit.MILLISECONDS).doOnNext { println("source4 emit:$it") }
    val subject = PublishSubject.create<String>()


    source4.map { "emit source4 : $it" }.subscribeOn(Schedulers.newThread()).subscribe(subject)
    source3.map { Thread.sleep(100); "emit source3 : $it" }.subscribeOn(Schedulers.newThread()).subscribe(subject)
    Thread.sleep(100)
    subject.observeOn(Schedulers.newThread()).subscribe(printObserver)

    Thread.sleep(2000)
}
