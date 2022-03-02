import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

fun main() {
//    usingExample1()
    usingMakesExceptionExample()
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