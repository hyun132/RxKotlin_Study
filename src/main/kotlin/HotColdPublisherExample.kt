import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.kotlin.Flowables
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

fun main() {
//    coldPublisher()
    hotPublisher()
}

fun coldPublisher(){
    val coldFlowable = Flowable.interval(1, TimeUnit.SECONDS)

    coldFlowable.subscribe({ println("A : $it") })
    Thread.sleep(2000)
    coldFlowable.subscribe({ println("B : $it") })

    Thread.sleep(3000)
}

fun hotPublisher(){
    val hotFlowable = Flowable.interval(1, TimeUnit.SECONDS).publish()
    hotFlowable.connect()

    hotFlowable.subscribe({ println("A : $it") })
    Thread.sleep(2000)
    hotFlowable.subscribe({ println("B : $it") })

    Thread.sleep(3000)
}
