package operator

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit

/**
 * observable을 concat하면
 * observableA 가 observableB이후에 동작해야 하는 경우
 * ex) 자동로그인을 한 결과에 따른 동작같은것 진행할 때때 */
fun main() {
    concatExample()
}

fun concatExample() {
    val test1 = Observable.just("1", "2", "3").delay(1, TimeUnit.SECONDS)
    val test2 = Observable.just("apple", "banana", "car")
    val test3 = Observable.interval(1, TimeUnit.SECONDS)

    Observable.concat(test1, test2).subscribeBy(
        { println("onError ${it.message}") },
        { println("complete") },
        { println(it) }
    )

    Thread.sleep(5000)

}