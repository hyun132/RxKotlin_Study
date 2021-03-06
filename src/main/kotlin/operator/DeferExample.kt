package operator

import io.reactivex.rxjava3.core.Observable
import java.util.*

fun main() {
//    usingDefer()
//    notDefer()
    useDefer()
}
private val colors = Arrays.asList("1", "3", "5", "6").iterator()

private fun getObservable(): Observable<String> {
    if (colors.hasNext()) {
        val color = colors.next()
        return Observable.just("$color-B", "$color-R", "$color-P") }
    return Observable.empty()
}

fun useDefer(){

    val observable1 = Observable.just(1,2,3,4,5)
    val observable2 = Observable.just("A","B","C","D","E")

    observable1.flatMap { num->
        observable2.map { "$it $num" }
    }.subscribe { println(it) }

}

fun usingDefer(){
    val supplier = {
        getObservable()
    }
    val source = Observable.defer<String>(supplier)
    source.subscribe {
            `val` -> println("subscriber #1 : $`val`")
    }
    source.subscribe {
            `val` -> println("subscriber #2 : $`val`")
    }
}

fun notDefer(){
    val source = getObservable()
    source.subscribe { `val` -> println("subscriber #1 : $`val`") }
    source.subscribe { `val` -> println("subscriber #2 : $`val`") }
}
