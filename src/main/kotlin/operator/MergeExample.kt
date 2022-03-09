package operator

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main() {
    val ob1 = Observable.just(1,2).doOnComplete { println("ob1 complete") }
    val ob2 = Observable.just(3,4,6).map { it*100 }
    println("----- merge() ------")
    Observable.merge(ob1, ob2)
        .subscribe{ println("merge 1,2 : $it") }
    println("----- mergeWith() ------")
    ob1.mergeWith(ob2)
        .subscribe{ println("mergeWith 1,2 :$it") }
    println("----- mergeArray() ------")
    val ob3 = Observable.interval(1000,TimeUnit.MILLISECONDS).map { it*10000 }
    val ob4 = Observable.interval(1000,TimeUnit.MILLISECONDS).map { it*10000 }
    val ob5 = Observable.interval(1000,TimeUnit.MILLISECONDS).map { it*10000 }
    Observable.mergeArray(ob1, ob2, ob3, ob4, ob5)
        .subscribe{  println("mergeArray :$it")
        }
Thread.sleep(5000)
}
