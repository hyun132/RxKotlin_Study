package datasource

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object FlowableDataSource {

    val flowableInterval = Flowable.interval(1000,TimeUnit.MILLISECONDS)
    val flowableJustInt = Flowable.just(1,2,3,4,5,6)
    val flowableJustString = Flowable.just("A","B","C","D")

}