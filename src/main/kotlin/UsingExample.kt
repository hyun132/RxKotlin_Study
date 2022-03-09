import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

fun main() {
    usingExample1()
//    usingMakesExceptionExample()
}

class SampleData() {
    val list = mutableListOf<Int>()

    init {
        (1..10).forEach { list.add(it) }
    }

    fun release() {
        println("released")
        list.clear()
    }
}


class SampleErrorData() {
    val list = mutableListOf<Int>()

    init {
        (1..10).forEach { list.add(it) }
    }

    fun release() {
        println("released")
        list.clear()
    }
}

/**
 * using은 dispose() 내부에 파라미터로 넘긴 resourceCleanup(리소스 해지해주는 부분)
 * 을 실행시켜줌으로써 리소스를 자동으로 해제해준다.
 * onComplete() onError() 모두 해제해줌.
 *
 * @Override
 * public void dispose() {
 *      if (eager) {
 *      disposeResource();
 */

fun usingExample1() {
    Observable.using(
        { SampleData() },
        { data -> data.list.toObservable() },
        { data -> data.release() }
    ).subscribeBy(
        onNext = { println(it) },
        onComplete = { println("complete") },
        onError = { println("error : ${it.message}") }
    )
}

fun usingMakesExceptionExample() {
    Observable.using(
        { SampleErrorData() },
        { data -> data.list.toObservable() },
        { data -> data.release() }
    ).subscribeBy(
        onNext = { if (it > 5) throw Exception("exception!") else println(it) },
        onComplete = { println("complete") },
        onError = { println("error : ${it.message}") }
    )
}