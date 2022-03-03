package operator

import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

fun main() {
    simpleFlatMapExample()
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
    Flowable.just(1,2,3,4,5)
        .flatMap {
            Flowable.just(it*it,it*it*it).delay(1,TimeUnit.SECONDS)
        }
        .subscribe { println("${Thread.currentThread().name} : $it") }

    Thread.sleep(3000)
}
