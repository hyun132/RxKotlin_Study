package operator

import io.reactivex.rxjava3.core.Observable

val duplicatedDataObservable = Observable.just(1, 1, 2, 3, 3, 3, 4, 5, 1)

fun main() {
//    distinctUntilChangedExample1()
    distinctExample1()
}

/**
 * distinctUntilChanged는 바로 이전에 입력한 값과 다른 값만 다운스트림으로 넘겨준다.
 *
 * 내부에서는 last라는 변수에 마지막으로 방출한 값을 들고있는다.
 * last = key; 를 통해 값을 갱신.
 *
 */
fun distinctUntilChangedExample1() {
    duplicatedDataObservable.doOnNext { println("emit : $it") }
        .distinctUntilChanged()
        .subscribe { println(it) }
}

/**
 * distinct는 방출한 적 없는 값만 다운스트림으로 넘겨준다.
 *
 * b = collection.add(key); 값이 collection에 없으면, true반환
 * if (b) {
 * downstream.onNext(value);
 *
 * 이처럼 collection에 본인이 방출한 값들을 저장하고 있다가 새로운 값이 들어왔을 때 한번도 방출한 적 없는 값만 다운스트림에 넘겨준다.
}
 */
fun distinctExample1() {
    duplicatedDataObservable.doOnNext { println("emit:$it") }
        .distinct()
        .subscribe{ println(it) }
}