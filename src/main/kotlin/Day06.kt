/**
 * https://adventofcode.com/2020/day/6
 * --- Custom Customs ---
 */
fun main() {
    val input = ResourceReader(6).text().split("\n\n")
    println(part1(input))
    println(part2(input))
}

private fun part1(input : List<String>) : Int {
    return input.map { it.replace("\n", "").groupBy { it } }.map { it.values.count() }.sum()
}

private fun part2(input : List<String>) : Int {
    return input.map { group ->
        group.replace("\n", "") to group.lines().size
    }.map {(group, size) ->
        group.fold("") {acc, next ->
            if(group.count { it == next } == size && !acc.contains(next)) acc + next
            else acc
        }
    }.map {
        it.length
    }.sum()
}