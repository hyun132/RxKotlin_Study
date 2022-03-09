package operator

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun main() {
    simpleSwitchMapExample()
}

fun simpleSwitchMapExample(){
    Observable.range(1,9)
        .switchMap { row ->
            Observable.range(1,9).map {
//                print("$row * $it = ")
                it*row
            }.observeOn(Schedulers.io())
        }.subscribe { println(it) }
}
