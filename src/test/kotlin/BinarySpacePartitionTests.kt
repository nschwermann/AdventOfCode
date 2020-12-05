import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.asserter

class BinarySpacePartitionTests{

    @Test
    fun `RowPartitions must be within 0 to 127`(){
        RowPartition(5..16)
        RowPartition(0..127)
        assertFails{
            RowPartition(-5 ..0)
        }
        assertFails{
            RowPartition(0 .. 128)
        }
    }

    @Test
    fun `ColumnPartitions must be within 0 to 7`(){
        ColumnPartition(0..7)
        ColumnPartition(1..6)
        assertFails {
            ColumnPartition(1..8)
        }
        assertFails {
            ColumnPartition(-1..7)
        }
    }

    @Test
    fun testLowerRangeCalculations(){
        assertEquals(0..63, (0..127).lower())
        assertEquals(32..47, (32..63).lower())
        assertEquals(4..5, (4..7).lower())
        assertEquals(4..4, (4..5).lower())
    }

    @Test
    fun testUpperRangeCalculations(){
        assertEquals(32..63, (0..63).upper())
        assertEquals(40..47, (32..47).upper())
        assertEquals(4 .. 7, (0..7).upper())
        assertEquals(5..5, (4..5).upper())
    }

    @Test
    fun `FBFBBFF becomes 44`(){
        val partition = "FBFBBFF".toRowPartition()
        println(partition.range)
        assertEquals( 44, partition.final)
    }

    @Test
    fun `RLR becomes 5`(){
        val partition = "RLR".toColumnPartition()
        assertEquals(5, partition.final)
    }

    @Test
    fun `FBFBBFFRLR seat with id 357`(){
        val location = "FBFBBFFRLR".findRowAndColumn()
        assertEquals(357, location.seatId)
    }
}