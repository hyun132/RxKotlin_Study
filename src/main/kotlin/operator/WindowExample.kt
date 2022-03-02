package operator

import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

fun main() {
//    windowTimeIntervalExample()
//    windowCountExample()
    windowCountExample2()
}

fun windowTimeIntervalExample() {
    val flowable = Flowable.interval(50, TimeUnit.MILLISECONDS)
    flowable.window(300, TimeUnit.MILLISECONDS) // 300ms동안 방출된 값들을 새로운 observable/flowable로 방출한다.
        .subscribe { longFlowable ->
            longFlowable.subscribe { print("$it ") }
            println("")
        }
    Thread.sleep(1000)

}


fun windowCountExample() {
    val flowable = Flowable.interval(100, TimeUnit.MILLISECONDS)
    flowable.window(2, 5)    //skip에 입력된 갯수만큼 데이터가 모였을 때 count의 값만큼 데이터를 가진 observable을 방출한다.
        .subscribe { longFlowable ->

            longFlowable.doOnSubscribe {
                println("doOnSubscribe")  // window에서 방출한 observable을 매번 구독하기 때문에 doOnSubscribe는 반복적으로 호출된다.ㅏ
            }.subscribe {
                    print("$it ")
                }

            println("")
        }
    Thread.sleep(3000)

}

// 잘못된 사용 방법인가?
fun windowCountExample2() {
    val flowable = Flowable.interval(100, TimeUnit.MILLISECONDS)
    flowable.window(5, 2)    //data 4개씩 모아서 새로운 observable 발행함.?? count가 skip보다 클 때 데이터가 반복됨..
        .subscribe { longFlowable ->
            longFlowable.subscribe {
                print("$it ")
            }
            println("")
        }
    Thread.sleep(3000)

}
