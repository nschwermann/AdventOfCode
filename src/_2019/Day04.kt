package _2019

import ResourceReader

fun main() {
    val range = ResourceReader(2019, 4).text().let {
        it.split("-")
    }.let {
        (it.first().toInt() .. it.last().toInt())
    }

    println("Part1 : ${part1(range)}")
    println("Part2 : ${part2(range)}")
}


/**
 * You arrive at the Venus fuel depot only to discover it's protected by a password. The Elves had written the password on a sticky note, but someone threw it out.

However, they do remember a few key facts about the password:

It is a six-digit number.
The value is within the range given in your puzzle input.
Two adjacent digits are the same (like 22 in 122345).
Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
Other than the range rule, the following are true:

111111 meets these criteria (double 11, never decreases).
223450 does not meet these criteria (decreasing pair of digits 50).
123789 does not meet these criteria (no double).
How many different passwords within the range given in your puzzle input meet these criteria?
 */
fun part1(range : IntRange) : Int{
    return range.count { meetsCriteria(it) }
}

fun meetsCriteria(int : Int) : Boolean {
    val string = int.toString()

    return int.toString().windowed(2,1,false).fold(false){ok, next ->
        ok || next[0] == next[1]
    } &&
    string.toList().windowed(2, 1, false).fold(true){ ok, next ->
        ok && next[0] <= next[1]
    }

}

/**
 * An Elf just remembered one more important detail: the two adjacent matching digits are not part of a larger group of matching digits.

Given this additional criterion, but still ignoring the range rule, the following are now true:

112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).
111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
How many different passwords within the range given in your puzzle input meet all of the criteria?
 */
fun part2(range : IntRange) : Int{
    return range.count { meetsCriteria2(it) }
}

fun meetsCriteria2(int : Int) : Boolean {

    return int.run {
        val s = toString()
        s.runs().any { it.num == 2 } &&
                (0 until 5).all { s[it] <= s[it+1] }
    }

}

data class CharRun(val item: Char, val num: Int)


fun String.runs() = sequence {
    val i = iterator()

    if(i.hasNext().not()) return@sequence
    var last = i.nextChar()
    var num = 1

    for(e in i) {
        if(last == e) num++
        else {
            yield(CharRun(last, num))
            last = e
            num = 1
        }
    }
    yield(CharRun(last, num))
}

