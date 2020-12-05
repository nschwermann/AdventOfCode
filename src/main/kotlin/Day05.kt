import kotlin.math.ceil
import kotlin.math.floor

/**
 * https://adventofcode.com/2020/day/5
 * --- Binary Boarding ---
 */
fun main() {
    val input = ResourceReader(5).lines().map { it.findRowAndColumn().seatId }
    println(part1(input))
    println(part2(input))
}

private fun part1(input : List<Int>) : Int{
    return input.maxOrNull()!!
}

private fun part2(input : List<Int>) : Int{
    return input.sorted().windowed(2, 1).find {
        it[0] != it[1] - 1
    }.let{it?.get(0)?.plus(1) ?: 0}
}

sealed class BinarySpacePartition(val range : IntRange){
    val final : Int = if(range.first == range.last) range.first else -1
}

class RowPartition(range : IntRange = 0 .. 127) : BinarySpacePartition(range){

    init {
        if(range.first < 0 || range.last > 127) throw IllegalArgumentException("Invalid Row Range")
    }

    val F : RowPartition by lazy { RowPartition(range.lower()) }
    val B : RowPartition by lazy { RowPartition(range.upper()) }
}

class ColumnPartition(range : IntRange = 0 .. 7) : BinarySpacePartition(range){
    init {
        if(range.first < 0 || range.last > 7) throw IllegalArgumentException("Invalid Column Range")
    }

    val L : ColumnPartition by lazy { ColumnPartition(range.lower()) }
    val R : ColumnPartition by lazy { ColumnPartition(range.upper()) }
}

fun IntRange.lower() : IntRange = if(first == last) this else first .. (first + floor((last - first)/ 2f).toInt())
fun IntRange.upper() : IntRange = if(first == last) this else (first + ceil((last - first)/ 2f).toInt()) .. last

fun String.toRowPartition() : RowPartition{
    return takeWhile {
        it == 'F' || it == 'B'
    }.fold(RowPartition()){acc, next ->
        if(next == 'F') acc.F
        else acc.B
    }
}

fun String.toColumnPartition() : ColumnPartition {
    return takeWhile {
        it == 'L' || it == 'R'
    }.fold(ColumnPartition()){acc, next ->
        if(next == 'L') acc.L
        else acc.R
    }
}

fun String.findRowAndColumn() : Pair<RowPartition, ColumnPartition>{
    return indexOfFirst { it == 'R' || it == 'L' }.let {
       this.toRowPartition() to substring(it).toColumnPartition()
    }
}

val Pair<RowPartition, ColumnPartition>.seatId : Int
    get() = first.final * 8 + second.final