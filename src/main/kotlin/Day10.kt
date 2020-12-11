import kotlin.math.pow

/**
 * --- Adapter Array ---
 * https://adventofcode.com/2020/day/10
 */
fun main() {
    val input = ResourceReader(10).ints()
    println(part1(input))
    println(part2(input))
}

private fun part1(input : List<Int>) : Int{
    data class Joltometer(var joltage : Int = 0, val differences  : MutableMap<Int, Int> = mutableMapOf(3 to 1))
    return input.sorted().fold(Joltometer()){acc, next ->
        val diff = next - acc.joltage
        acc.joltage = next
        acc.differences[diff] = acc.differences.getOrDefault(diff, 0) + 1
        acc
    }.also { println(it) }.let {
        it.differences[1]!! * it.differences[3]!!
    }
}

private fun part2(input: List<Int>) : Long =  AdapterPathCounter.countPaths(input)

object AdapterPathCounter {

    /**
     * If there are 3 adapters in a row with a difference of one 'jolt' there are 4 combinations,
     * 4 will have 7 combinations and 2 has 2 combinations.
     * These happen to be [Tribonacci Numbers](https://en.wikipedia.org/wiki/Generalizations_of_Fibonacci_numbers#Tribonacci_numbers)
     */
    fun countPaths(input: List<Int>) : Long {
        val modified = input.toMutableList().apply {
            add(0)
            sort()
        }
        var index = 0
        val streaks =  mutableMapOf<Int, Int>()
        while(index < modified.size){
            var cur = modified[index]
            var streak = 0
            var look = index + 1
            while(look < modified.size && (modified[look] - cur) == 1){
                cur = modified[look]
                streak++
                look++
            }
            index = look
            streaks[streak] = streaks.getOrDefault(streak, 0) + 1
        }

        return streaks.mapKeys {
            when (it.key) {
                2 -> 2
                3 -> 4
                4 -> 7
                else -> 1
            }
        }.also {
            println(it)
        }.map {
            it.key.toDouble().pow(it.value)
        }.also { println(it) }.let {
            it.fold(1.0){ acc, next -> acc*next}
        }.toLong()
    }
}
