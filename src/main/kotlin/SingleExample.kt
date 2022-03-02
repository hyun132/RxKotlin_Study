import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy


/**
 * observable과는 다르게 데이터가 1건 or error발생만 통지하기때문에
 * onNext대신 onSuccess가 있으며 onSuccess/onError가 호출되면 종료한다.
 * onComplete가 없음.
 */
fun main() {

    val single = Single.create<String> { // 하나의 값을 통지함.
        it.onSuccess("hello")
    }
    val emptySingle = Observable.empty<Any>().single("default")

    single.subscribeBy { println("onSuccess $it") }
    emptySingle.subscribeBy(
        onSuccess = { println("onSuccess $it") },
        onError = { println(it.message) }
    )
}