import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun main() {
    observableFlow()
}

fun flowableFlow(){
    Flowable.just("10","2","6","12","8","5")
        .subscribeOn(Schedulers.io())
        .map {
            println("map $it : ${Thread.currentThread().name}")
            it.toInt()
        }
        .observeOn(Schedulers.computation())
        .filter {
            println("filter $it : ${Thread.currentThread().name}")
            it % 3 == 0
        }
        .flatMap { Flowable.just(it, it * it) }
        .observeOn(Schedulers.newThread())
        .subscribe { println("take emitted $it at ${Thread.currentThread().name}") }

    Thread.sleep(1000)
}

fun observableFlow(){
    Observable.just("10","2","6","12","8","5")
        .subscribeOn(Schedulers.io())
        .map {
            println("map $it : ${Thread.currentThread().name}")
            it.toInt()
        }
        .observeOn(Schedulers.computation())
        .filter {
            println("filter $it : ${Thread.currentThread().name}")
            it % 3 == 0
        }
        .flatMap { Observable.just(it, it * it) }
        .observeOn(Schedulers.newThread())
        .subscribe { println("take emitted $it at ${Thread.currentThread().name}") }

    Thread.sleep(1000)
}
