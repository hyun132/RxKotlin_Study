class PureFunctionExample {
}

fun main() {

    val numbers = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9)

    var sumEvenNumbers = 0
    for (num in numbers) {
        if (num % 2 == 0) sumEvenNumbers += num
    }
    println(sumEvenNumbers)


    println(
        numbers
        .filter { it % 2 == 0 }
        .reduce { acc, i -> acc + i }
    )

}
