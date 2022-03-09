package operator

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.Flow

fun main() {
    mapFunction()
}

fun mapFunction() {
    Observable.fromArray("aaa", "bbb", "ccc", "ddd")
        .map { it[0].code }
        .subscribe(object : Observer<Int>{
            override fun onSubscribe(d: Disposable) {
                println("onSubscribe")
            }

            override fun onNext(t: Int) {
                println("next $t")
            }

            override fun onError(e: Throwable) {
                println("onError ${e.message}")
            }

            override fun onComplete() {
                println("onComplete")
            }

        })
}
