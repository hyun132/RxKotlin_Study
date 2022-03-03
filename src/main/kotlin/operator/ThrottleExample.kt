package operator

import datasource.ObservableDataSource.observableInterval
import datasource.ObservableDataSource.observableInterval2
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {
//    throttleFirstExample()
    throttleLastExample()
}

/**
 * throttle은 정해진 시간 동안에 들어온 데이터를 내보내 주는 연산자이다.
 * throttleFirst는 해당 기간 내에 가장 먼저 들어온 값을 내보내고
 * throttleLast는 해당 기간 내에 가장 나중에 들어온 값을 내보낸다.
 * 만약 그  기간 내에 데이터가 들어오지 않았다면, 값을 내보내지 않는다.
 *
 */

fun throttleFirstExample(){
    observableInterval2
        .doOnNext { println("emit : $it (${System.currentTimeMillis()/ 1000 % 60})") }
        .subscribeOn(Schedulers.newThread())
        .throttleFirst(1000,TimeUnit.MILLISECONDS)
        .subscribeBy(
            onNext = { println("$it (${System.currentTimeMillis()/ 1000 % 60})") }
        )

    Thread.sleep(5000)
}

/**
 * throttleLast 의 내부 연산자는 smaple이다.
 * 다만 sample은 emitLast값에 따라 첫번째 값을 방출할 것인지
 * 마지막 값을 방출할 것인지를 결정한다.
 *
 * if (emitLast) {
 * source.subscribe(new SampleTimedEmitLast<>(serial, period, unit, scheduler));
 * } else {
 * source.subscribe(new SampleTimedNoLast<>(serial, period, unit, scheduler));
 * }
 *
 */

fun throttleLastExample(){
    observableInterval2
        .doOnNext { println("emit : $it (${System.currentTimeMillis()/ 1000 % 60})") }
        .subscribeOn(Schedulers.newThread())
        .throttleLast(1000,TimeUnit.MILLISECONDS)
        .subscribeBy(
            onNext = { println("$it (${System.currentTimeMillis()/ 1000 % 60})") }
        )

    Thread.sleep(5000)
}