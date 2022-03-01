import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

fun main() {
    Flowable.just(1,2,3,4,5)
        .subscribeOn(Schedulers.computation())
        .subscribeOn(Schedulers.io())
        .subscribeOn(Schedulers.single())
        .subscribe{ println("$it on ${Thread.currentThread().name}")}

    Thread.sleep(100)
}
