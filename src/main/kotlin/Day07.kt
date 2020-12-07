import java.util.*

/**
 * https://adventofcode.com/2020/day/7
 * --- Handy Haversacks ---
 */

fun main() {
    val parentPattern = "(\\w+ \\w+) bags contain (.*)".toRegex()
    val containsPattern = "(\\d+) (\\w+ \\w+)".toRegex()
    val input = ResourceReader(7).lines().map {
        parentPattern.find(it)!!
    }.map {
        val (color, contains) = it.destructured
        val list = containsPattern.findAll(contains).map {
            val (num, color) = it.destructured
            num.toInt() to Bag(color, emptyList())
        }.toList()
        Bag(color, list)
    }
    println(part1(input))
    println(part2(input))
}

@OptIn(ExperimentalStdlibApi::class)
private fun part1(input : List<Bag>) : Int {
    val edges = buildMap<Bag, List<Bag>> {
            input.forEach {bag ->
                put(bag, emptyList())
                bag.contains.forEach {
                    val list = get(bag) ?: emptyList()
                    put(bag, list + it.second)
                }
            }
        }

    return input.count { bagContains(edges, it, Bag("shiny gold")) }
}

private fun part2(input : List<Bag>) : Int {
    return input.find { it == Bag("shiny gold") }?.let{findBagsInside(it, input)}!!
}

private fun findBagsInside(bag : Bag, input: List<Bag>) : Int = when{
    bag.contains.isEmpty() -> 0
    else -> bag.contains.map { it.first }.sum() + bag.contains.fold(0){acc, (count, bag) ->
        acc + (count * findBagsInside(input.find { it == bag }!!, input))
    }
}

private fun bagContains(edges : Map<Bag, List<Bag>>, start: Bag, lookingFor : Bag) : Boolean{
    if(start == lookingFor) return false
    val que = LinkedList<Bag>()
    que.add(start)
    while(que.peek() != null){
        val next = que.poll()
        if(next.visited) continue
        if(next == lookingFor) return true
        edges[next]?.let(que::addAll)
    }
    return false
}

private data class Bag(val color : String,
                       val contains : List<Pair<Int, Bag>> = emptyList(),
                       var visited : Boolean = false){
    override fun equals(other: Any?): Boolean {
        return if(other is Bag) other.color == color
        else false
    }

    override fun hashCode(): Int {
        return color.hashCode()
    }
}