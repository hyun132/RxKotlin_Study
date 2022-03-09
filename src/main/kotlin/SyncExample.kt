import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {
//    intervalExample()
//    Thread.sleep(4000)
//    println("_____")
//    justExample()
//    chainingExample()
//    chainingExample2()
    reactiveStream()
}

fun reactiveStream(){
    Observable.range(1,10)
        .filter{it%2==0}
        .map { "$it 는 짝수" }
        .subscribe {
            println(it)
        }
}

fun intervalExample() {
    /**
     * 다른 스레드에서 처리 작업
     */
    Flowable.interval(1000L, TimeUnit.MILLISECONDS)
        .doOnNext { println("emit $it ${System.currentTimeMillis()} in ${Thread.currentThread().name}") }
        .take(3)
        .subscribe { Thread.sleep(500L); println("in ${Thread.currentThread().name}") }

    println("end")
}

fun justExample() {
    /**
     * just, from처럼 미리 생성된 데이터 통지하는 생산자는 메인 스레드에서 동작
     */
    Flowable.just(1, 2, 3)
        .doOnNext { Thread.sleep(1000L);println("emit $it ${System.currentTimeMillis()} in ${Thread.currentThread().name}") }
        .subscribe { Thread.sleep(500L); println("in ${Thread.currentThread().name}") }

    println("end")
}

fun chainingExample() {
    Flowable.range(1, 50)
        .doOnNext { println("emit $it : ${Thread.currentThread().name}") }
        .subscribeOn(Schedulers.trampoline())
        .observeOn(Schedulers.computation())
        .filter {
            println("filter $it : ${Thread.currentThread().name}")
            it % 2 == 0
        }
        .observeOn(Schedulers.io())
        .map {
            println("map $it : ${Thread.currentThread().name}")
            it * it
        }
//        .flatMap { it ->
//            Flowable.just(it, it * it).subscribeOn(Schedulers.newThread())
//        }.doOnNext { num -> "in flatMap : $num emitted at ${Thread.currentThread().name}" }
        .observeOn(Schedulers.io())
        .take(10)
        .subscribe { println("take emitted $it at ${Thread.currentThread().name}") }

    Thread.sleep(1000)
}

fun chainingExample2() {
    Flowable.range(1, 10)
        .doOnNext {
            Thread.sleep(10)
            println("emit $it : ${Thread.currentThread().name}")
        }
        .subscribeOn(Schedulers.trampoline())
        .observeOn(Schedulers.computation())
        .map {
            println("map $it : ${Thread.currentThread().name}")
            it * it
        }
        .observeOn(Schedulers.io())
        .filter {
            println("filter $it : ${Thread.currentThread().name}")
            it % 2 == 0
        }
        .flatMap { Flowable.just(it, it * it) }
        .take(10)
        .observeOn(Schedulers.newThread())
        .subscribe { println("take emitted $it at ${Thread.currentThread().name}") }

    Thread.sleep(1000)
}

