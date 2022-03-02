import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy

fun main() {

    val single = Maybe.create<String> { // 하나의 값을 통지함.
        it.onSuccess("hello")
//        it.onError(throw Exception("error occurred"))
//        it.onComplete()
    }

    single.subscribeBy(
        onError = { println("onError : ${it.message}") },
        onComplete = { println("onComplete") },  //데이터 통지하지 않고 종료할 때
        onSuccess = { println("onSuccess $it") } // 데이터 통지하고 종료할때
    )
}