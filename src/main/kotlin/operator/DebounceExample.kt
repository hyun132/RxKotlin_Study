package operator

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * 얘는 마지막으로 데이터 들어온 시간부터 timeout값 만큼의 시간이 흐를 동안
 * 다음 입력이 들어오지 않았을 때 마지막 값을 방출함.
 *
 *        @Override
 * public void onNext(T t) {
 * if (done) {
 * return;
 * }
 * long idx = index + 1;
 * index = idx;
 *
 * Disposable d = timer;
 * if (d != null) {
 * d.dispose();
 * }
 *
 * DebounceEmitter<T> de = new DebounceEmitter<>(t, idx, this);
 * timer = de;
 *
 *
 * 위처럼 timer이미 예약된 방출될 값이 있으면 dispose하고 새로운 emitter를 만들어 예약한다.
 * 얘는 일반적으로 검색api호출할 때 많이 사용한다. 모든 입력에 대해 api호출을 하는게 아닌
 * 사용자가 검색어를 입력하고 일정 시간이 지나면 데이터를 방출하여api를 호출하도록 한다.
 */
fun main() {
    val debounceObservable = Observable.create<Int> {
        it.onNext(1)
        Thread.sleep(50)
        it.onNext(2)
        Thread.sleep(500)
        it.onNext(3)

        Thread.sleep(10)
    }

    debounceObservable.debounce(100,TimeUnit.MILLISECONDS).subscribe { println(it) }
}

/**
 * 이런식으로 사용한다.
 *
 * val observableTextQuery = searchEditText.textChanges()
 * .debounce(500, TimeUnit.MILLISECONDS)
 * .subscribeOn(Schedulers.io())
 * .subscribe { charSequence ->
 * getResultList(charSequence.toString())
 * }
 */