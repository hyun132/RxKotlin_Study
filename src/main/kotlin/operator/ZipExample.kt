package operator

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

fun main() {
    simpleZipExample()
}

fun simpleZipExample() {
    val observable2 =
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).doOnNext { println("emit2 $it") }.subscribeOn(Schedulers.newThread())
            .doOnComplete { println("Complete2") }
    val observable1 = Observable.just('a', 'b', 'c', 'd', 'e', 'f').doOnNext { Thread.sleep(50);println("emit1 $it") }
        .subscribeOn(Schedulers.io()).doOnComplete { println("Complete1") }
    Observable.zip(observable1, observable2, { a, b -> "$a$b" }).subscribeBy(
        onNext = { println(it) }, onComplete = { println("subscriber complete") }
    )
    Thread.sleep(1000)
}
