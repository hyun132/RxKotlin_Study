package datasource

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object ObservableDataSource {

    val observableInterval = Observable.interval(1000,TimeUnit.MILLISECONDS)
    val observableJustInt = Observable.just(1,2,3,4,5,6)
    val observableJustString = Observable.just("A","B","C","D")

}