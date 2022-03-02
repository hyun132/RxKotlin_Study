package operator

import datasource.FlowableDataSource.flowableJustString
import datasource.ObservableDataSource.observableJustString
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable


/**
 * toList는 upstream의 데이터가 모두 방출될 때 까지 받은 값을 버퍼에 저장해서
 * 한번에 리스트로 내보내줌. 하나의 리스트로 방출해주기 때문에 리턴타입이 Single
 */
fun main() {
    defaultToListObservableExample()
//    defaultToListFlowableExample()
}

fun defaultToListObservableExample(){
    observableJustString.toList().subscribe(object : SingleObserver<MutableList<String>> {
        override fun onSubscribe(d: Disposable?) {
            println("onSubscribe")
        }

        override fun onSuccess(t: MutableList<String>?) {
            println("onSuccess $t")
        }

        override fun onError(e: Throwable?) {
            println("onError ${e?.message}")
        }

    })
}

fun defaultToListFlowableExample(){
    flowableJustString.toList().subscribe(object : SingleObserver<MutableList<String>> {
        override fun onSubscribe(d: Disposable?) {
            println("onSubscribe")
        }

        override fun onSuccess(t: MutableList<String>?) {
            println("onSuccess $t")
        }

        override fun onError(e: Throwable?) {
            println("onError ${e?.message}")
        }

    })


}