import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * CompositeDisposable은 여러 Disposable을 모아 복수의 구독을 동시에 해지할 수 있다.
 */
fun main() {
    val compositeDisposable = CompositeDisposable()

    val flowableA = Flowable.range(1,10)
        .doOnCancel { println("flowable A is canceled") }
        .observeOn(Schedulers.computation())
        .subscribe{
            Thread.sleep(10)
            println("A's data is $it")}

    val flowableB = Flowable.range(1,10)
        .doOnCancel { println("flowable B is canceled") }
        .observeOn(Schedulers.computation())
        .subscribe{
            Thread.sleep(10)
            println("B's data is $it")}

    compositeDisposable.addAll(flowableA,flowableB)
    Thread.sleep(50)
    compositeDisposable.dispose()
}
