package operator

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit

/**
 * window랑 비교해볼 것.
 */
fun main() {
//    bufferExample()
//    bufferCountExample()
    bufferCountExample2()
}

fun bufferExample() {
    val observable = Observable.interval(100, TimeUnit.MILLISECONDS)
    val intervalTime = Observable.interval(200, TimeUnit.MILLISECONDS)
    val time = System.currentTimeMillis()
    observable.buffer(intervalTime)
        .subscribe {
            println("Time: ${System.currentTimeMillis() - time} : $it")
        }
    Thread.sleep(1000)
}

fun bufferCountExample() {
    val observable = Observable.interval(100, TimeUnit.MILLISECONDS)
    val time = System.currentTimeMillis()
    observable.buffer(3)
        .subscribe {
            println("Time: ${System.currentTimeMillis() - time} : $it")
        }
    Thread.sleep(1000)
}

fun bufferCountExample2() {
    val observable = Observable.interval(100, TimeUnit.MILLISECONDS).doOnComplete { println("observable complete!") }
    val time = System.currentTimeMillis()
    observable
        .take(10)
        .buffer(3, 5)
        .doOnSubscribe {
            println("subscribe")
        }  //skip개 만큼의 데이터를 받으면 data를 count개 만큼 observable에 담아서 방출한다.
        .doOnComplete {
            println("doOnComplete")
        }
        .subscribeBy(
            onNext = { println("Time: ${System.currentTimeMillis() - time} : $it") },
            onComplete = { println("complete") },
            onError = { println("error : ${it.message}") }
        )


    Thread.sleep(2000)

    /**
    Time: 316 : [0, 1, 2]
    Time: 813 : [5, 6, 7]
    데이터가 count개가 다 모여야 방출함. 그 전에 종료되면 마지막에 담긴 데이터들은 방출하지 않고 종료
     */
}