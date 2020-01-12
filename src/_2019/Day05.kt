package _2019

import ResourceReader

fun main() {
    val input = ResourceReader(2019, 5).intArray()

    println("Part1 : ${IntCodeComputer.runProgram(input.copyOf(), 1)}")
    println("Part2 : ${IntCodeComputer.runProgram(input.copyOf(), 5)}")

}

