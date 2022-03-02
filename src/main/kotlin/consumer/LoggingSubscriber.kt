package consumer

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

object LoggingSubscriber {
    val defaultSubscriber = object : Subscriber<Long> {
        lateinit var subscription: Subscription
        override fun onSubscribe(s: Subscription?) {
            if (s != null) {
                subscription = s
            }
            s?.request(1)
        }

        override fun onNext(t: Long?) {
            println("current value is $t")
            subscription.request(1)
        }

        override fun onError(t: Throwable?) {
            println("onError ${t?.message}")
        }

        override fun onComplete() {
            println("onComplete")
        }
    }
}