import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.lang.Exception

val makeExceptionObservable = Observable.range(1,10)
    .map {
        if(it==3) throw Exception("error occurred")
        else it
    }

fun main() {
//    errorExampleWithOnErrorFunction()
//    errorExampleWithOnErrorReturn()
    errorExampleWithRetry()
}

fun errorExampleWithOnErrorFunction(){
    makeExceptionObservable
        .subscribeBy (
            onNext = { println("get $it")},
            onError = { println("error : ${it.message}")},
            onComplete = { println("onComplete")}
                )
}

fun errorExampleWithOnErrorReturn(){
    makeExceptionObservable
        .onErrorReturnItem(-1)
        .subscribeBy (
            onNext = { println("get $it")},
//            onError = { println("error : ${it.message} from onErrorReturnItem()")},
            onComplete = { println("onComplete")}
        )
}

fun errorExampleWithRetry(){
    makeExceptionObservable
        .retry(2)
        .subscribeBy (
            onNext = { println("get $it")},
            onError = { println("error : ${it.message}")},
            onComplete = { println("onComplete")}
        )
}
