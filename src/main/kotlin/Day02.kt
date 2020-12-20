/**
 * https://adventofcode.com/2020/day/2
 * --- Password Philosophy ---
 */
private fun main() {
    val input = ResourceReader(2).lines().map {
        it.split('-', ' ', ':').let {
            Policy(it[0].toInt(), it[1].toInt(), it[2].first()) to it[4]
        }
    }
    println(part1(input))
    println(part2(input))
}

private data class Policy(val min : Int, val max : Int, val char: Char)

private fun part1(list: List<Pair<Policy, String>>) : Int {
    return list.count {(policy, password) ->
        password.count { policy.char == it } in policy.min .. policy.max
    }
}

private fun part2(list: List<Pair<Policy, String>>) : Int {
    return list.count { (policy, password) ->
        (password[policy.min - 1] == policy.char) xor (password[policy.max - 1] == policy.char)
    }
}