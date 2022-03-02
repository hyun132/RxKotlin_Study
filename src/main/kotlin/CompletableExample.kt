import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import java.util.concurrent.TimeUnit

fun main() {
//    singleExample()
    singleExample2()
}

fun singleExample(){

    val single = Completable.create {
//        it.onError(throw Exception("error occurred"))
        it.onComplete()
    }

    single.subscribeBy(
        onError = { println("onError : ${it.message}") },
        onComplete = { println("onComplete") },  //데이터 통지하지 않고 종료할 때
    )
}

fun singleExample2(){
    val disposable = Completable.complete().delay(1000,TimeUnit.MILLISECONDS)
        .subscribeWith(object :DisposableCompletableObserver(){
            override fun onComplete() {
                println("onComplete")
            }

            override fun onError(e: Throwable?) {
                println("onError : ${e?.message}")
            }
        })

    Thread.sleep(2000)
//    disposable.dispose()
    disposable.onError(Exception("error occurred!"))
}

//@CheckReturnValue
//@SchedulerSupport(SchedulerSupport.NONE)
//@NonNull
//public final <@NonNull E extends CompletableObserver> E subscribeWith(E observer) {
//    subscribe(observer);
//    return observer;
//}
//
//public final Disposable subscribe(@NonNull Action onComplete, @NonNull Consumer<? super Throwable> onError) {
//    Objects.requireNonNull(onError, "onError is null");
//    Objects.requireNonNull(onComplete, "onComplete is null");
//
//    CallbackCompletableObserver observer = new CallbackCompletableObserver(onError, onComplete);
//    subscribe(observer);
//    return observer;
//}