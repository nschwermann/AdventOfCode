/**
 * --- Rambunctious Recitation ---
 * https://adventofcode.com/2020/day/15
 */
private fun main() {
    val input = listOf(2,1,10,11,0,6)
    println(solver(input, 2020L))
    println(solver(input, 30_000_000))
}

private fun solver(input : List<Int>, spoken : Long) : Long {
    val track = input.foldIndexed(mutableMapOf<Long, Long>()){index,acc, next ->
        acc.apply {
            put(next.toLong(), index + 1L)
        }
    }
    val turns = LongRange(input.size + 1L, spoken)
    return turns.runningFold(0L){acc, turn ->
        track.getOrDefault(acc, 0L).let { last ->
            if(!track.containsKey(acc)){
                0
            } else {
                turn - last
            }.also {
                track[acc] = turn
            }
        }
    }.dropLast(1).last()
}