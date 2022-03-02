package consumer

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

object LoggingObserver {
    val defaultObserver = object : Observer<Long> {
        override fun onNext(t: Long?) {
            println("current value is $t")
        }

        override fun onError(t: Throwable?) {
            println("onError ${t?.message}")
        }

        override fun onComplete() {
            println("onComplete")
        }

        override fun onSubscribe(d: Disposable?) {
            println("onSubscribe")
        }
    }

    val defaultStringObserver = object : Observer<String> {
        override fun onNext(t: String?) {
            println("current value is $t")
        }

        override fun onError(t: Throwable?) {
            println("onError ${t?.message}")
        }

        override fun onComplete() {
            println("onComplete")
        }

        override fun onSubscribe(d: Disposable?) {
            println("onSubscribe")
        }
    }
}