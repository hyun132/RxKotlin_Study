package operator

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {
//    simpleFlatMapExample()
    flatmapGuguExample()
}

/**
 * flat, concat, switch  -> FlowableScalarXMap ScalarXMapFlowable
 * else -> FlowableFlatMap
 *
 * subscribe 를 하게 되면
 *         public void onSubscribe(Subscription s) {
 * if (SubscriptionHelper.validate(this.upstream, s)) {
 * this.upstream = s;
 * downstream.onSubscribe(this);
 *
 *
 */

fun simpleFlatMapExample(){
    Flowable.just(1,)
        .flatMap {
            Flowable.just(it*it,it*it*it,it*it*it*it).delay(1,TimeUnit.SECONDS)
        }
        .subscribe { println("${Thread.currentThread().name} : $it") }

    Thread.sleep(3000)
}

/**
 * 얘는 flatmap내의 observer의 스레드 변경해주면 받아오는 데이터의 순서가 보장되지 않음.
 */
fun flatmapGuguExample(){
    Observable.range(1,9)
        .flatMap { row ->
            Observable.range(1,9).map {
//                print("$row * $it = ")
                it*row
            }.observeOn(Schedulers.io())
        }.subscribe { println(it) }
}
