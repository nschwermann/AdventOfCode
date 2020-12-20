/**
 * --- Encoding Error ---
 * https://adventofcode.com/2020/day/9
 */
private fun main() {
    val input = ResourceReader(9).lines().map { it.toLong() }.toLongArray()
    println(part1(input))
    println(part2(input))
}

private fun part1(input : LongArray) : Long {
    return XMAS(input, 25).findFirst()
}

private fun part2(input : LongArray) : Long {
    val xmas = XMAS(input, 25)
    return xmas.findCrack(xmas.findFirst())
}

class XMAS(private val data : LongArray, private val preamble : Int){

    fun findFirst() : Long {
        for(index in (preamble + 1) until data.size){
            val current = data[index]
            val slice = data.slice((index - preamble - 1) until index).filterNot {
                it == current || it > current
            }
            if(!hasMatch(slice, current)) return current
        }
        return -1
    }

    fun findCrack(target : Long) : Long{

        var total = 0L
        var startingIndex = 0
        var finalIndex = 0

        fun check(): Long?{
            return if(total == target)  data.slice(startingIndex .. finalIndex).let {
                it.minOrNull()!! to it.maxOrNull()!!
            }.let {
                it.first + it.second
            } else null
        }

        for(index in data.indices){
            finalIndex = index
            total += data[index]
            val check = check()
            if(check != null) return check
            while(total > target) {
                total -= data[startingIndex]
                startingIndex++
                val check = check()
                if(check != null) return check
            }
        }
        return -1L
    }

    private fun hasMatch(slice : List<Long>, target : Long) : Boolean{
        for(i in slice.indices){
            val cur = slice[i]
            val want = target - cur
            if(want == target) continue
            if(slice.contains(want)) return true
        }
        return false
    }
}