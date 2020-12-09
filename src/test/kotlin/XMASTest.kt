import org.junit.Test

import org.junit.Assert.*

class XMASTest {

    @Test
    fun testXMAS() {
        val input ="""
            35
            20
            15
            25
            47
            40
            62
            55
            65
            95
            102
            117
            150
            182
            127
            219
            299
            277
            309
            576
        """.trimIndent().lines().map { it.toLong() }.toLongArray()
        val xmas = XMAS(input, 5)
        assertEquals(127, xmas.findFirst())
        assertEquals(62, xmas.findCrack(xmas.findFirst()))
    }
}