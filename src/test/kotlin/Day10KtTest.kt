import org.junit.Assert.*
import kotlin.test.Test

class Day10KtTest{

    @Test
    fun `Series that should be 4`(){
        val input = """
            3
            1
            6
            2
        """.trimIndent().lines().map { it.toInt() }
        assertEquals(4,AdapterPathCounter.countPaths(input))
    }

    @Test
    fun `Series that should be 8`(){
        val input = """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent().lines().map { it.toInt() }
        assertEquals(8, AdapterPathCounter.countPaths(input))
    }

    @Test
    fun `Series that should be 19208`(){
        val input = """
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
    """.trimIndent().lines().map { it.toInt() }
        assertEquals(19208,AdapterPathCounter.countPaths(input))
    }

    @Test
    fun `Series that should be 28`(){
        val input = """
            17
            6
            10
            5
            13
            7
            1
            4
            12
            11
            14
        """.trimIndent().lines().map { it.toInt() }
        assertEquals(28, AdapterPathCounter.countPaths(input))
    }

    @Test
    fun `Series that should be 7`(){
        val input = """
            4
            11
            7
            8
            1
            6
            5
        """.trimIndent().lines().map { it.toInt() }
        assertEquals(7, AdapterPathCounter.countPaths(input))
    }

    @Test
    fun `Another Series that should be 4`(){
        val input = """
            10
            6
            4
            7
            1
            5
        """.trimIndent().lines().map { it.toInt() }
        assertEquals(4, AdapterPathCounter.countPaths(input))
    }

}