/**
 * --- Shuttle Search ---
 * https://adventofcode.com/2020/day/13
 */
private fun main() {

    val input = ResourceReader(13).lines()

    println(part1(input))
    println(part2(input[1]))
}

private fun part1(input : List<String>) : Int{
    val (timestamp, busIds) = input.let {
        it.first().toInt() to it[1].split(',').map { it.toIntOrNull() }.filterNotNull()
    }
    val closestTimes = busIds.map {
        val iterator = (1..timestamp).iterator()
        var next = 0
        while(next <= timestamp){
            next = it * iterator.nextInt()
        }
        next
    }
    val nextDeparture = closestTimes.minOrNull()!!
    val busId = busIds[closestTimes.indexOf(nextDeparture)]
    val waitTime = nextDeparture - timestamp
    return waitTime * busId
}

private fun findClosestToTarget(target : Long, ids : List<Int>, cache : List<MulIterator>) : List<Long>{

    return ids.mapIndexed { index, _ ->
        val iterator = cache[index]
        var next = if(iterator.hasPrevious()) iterator.previous() else iterator.next()

        while(next < target){
            next = iterator.next()
        }
        next
    }
}


private val largerThan = 100_000_000_000_000

private fun part2(input : String) : Long {
    val busIds = input.split(',').map {
        it.toIntOrNull()
    }

//    val busIds = listOf(1789,37,47,1889)
//    val busIds = listOf(17,null,13,19)
    val filteredIds = busIds.filterNotNull()
    val deltas = mutableListOf<Int>()
    val multiples = filteredIds.map { MulIterator(it) }
    run{
        var index = 0
        var streak = 0
        while(index < busIds.size){

            if(busIds[index] != null){
                deltas += streak
                streak = 1
                var look = index + 1
                while(look < busIds.size && busIds[look] == null){
                    look++
                    streak++
                }
                index = look
            }
        }
    }

    println(busIds.filterNotNull())
    println(deltas)


    var printCheck = 1L
    var skipAhead = 0L
    for(mul in MulIterator(filteredIds.first())){
        if(skipAhead < largerThan){
            skipAhead = mul
        } else {
            break
        }
    }
    println("using $skipAhead for start")
    val progression = LongProgression.fromClosedRange(skipAhead, Long.MAX_VALUE, filteredIds.first().toLong())
    for(timeStamp in progression){

        printCheck++
        if(printCheck % 500_000_000 == 0L){
            println("trying $timeStamp")
        }

        val targets = findClosestToTarget(timeStamp, filteredIds, multiples)
        val needed = deltas.drop(1).fold(mutableListOf(timeStamp)){acc, delta ->
            acc.apply {
                plusAssign(last() + delta)
            }
        }

        if(needed == targets){
            return timeStamp
        }

    }
    return 1L
}

class MulIterator(val multiple: Int) : ListIterator<Long> {
    private var index = 1L
    private var previous : Long? = null
    private val sequence = generateSequence {
        (multiple * index).also {
            previous = it
            index++
        }
    }.iterator()

    override fun hasNext(): Boolean {
        return index < Long.MAX_VALUE
    }

    override fun hasPrevious(): Boolean {
        return previous != null
    }

    override fun next(): Long {
        return sequence.next()
    }

    override fun nextIndex(): Int {
        return index.toInt() + 1
    }

    override fun previous(): Long {
        return previous!!
    }

    override fun previousIndex(): Int {
        return index.toInt()
    }
}