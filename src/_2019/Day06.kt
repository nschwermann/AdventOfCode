package _2019

import ResourceReader

fun main() {
    val input = ResourceReader(2019, 6).lines().map {
        it.split(")")
    }.map {
        it.first() to it.last()
    }

    val test = """COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L""".lines().map { it.split(")") }.map {
        DirectOrbit(it.first(), it.last())
    }

    val count = test.map { test.chain(it.parent).size }.sum()
    println("count $count")


}

fun List<DirectOrbit>.chain(body : String, chain : List<String> = emptyList()) : List<String> {
    return firstOrNull{ it.parent == body} ?.let {
        chain(it.child, chain + body)
    } ?: chain
}
data class DirectOrbit(val parent: String, val child : String)