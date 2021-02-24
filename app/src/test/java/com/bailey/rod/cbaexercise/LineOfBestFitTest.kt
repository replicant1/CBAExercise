package com.bailey.rod.cbaexercise

import com.bailey.rod.cbaexercise.stat.XYDataPoint
import com.bailey.rod.cbaexercise.stat.XYDataSet
import dalvik.annotation.TestTarget
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LineOfBestFitTest {

    @Test
    fun twoPointsSlopingUp() {
        val a = XYDataPoint(0.0, 0.0)
        val b = XYDataPoint(1.0, 1.0)
        val series = XYDataSet(arrayOf(a, b))

        val line = series.lineOfBestFit()

        assertEquals(0.0, line.b, MARGIN)
        assertEquals(1.0, line.m, MARGIN)
    }

    @Test
    fun orderDoesntMatter() {
        val a = XYDataPoint(1.0, 1.0)
        val b = XYDataPoint(0.0, 0.0)
        val series1 = XYDataSet(arrayOf(a, b))
        val series2 = XYDataSet(arrayOf(b, a))
        val line1 = series1.lineOfBestFit()
        val line2 = series2.lineOfBestFit()

        assertEquals(0.0, line1.b, MARGIN)
        assertEquals(0.0, line2.b, MARGIN)
        assertEquals(1.0, line1.m, MARGIN)
        assertEquals(1.0, line2.m, MARGIN)
    }

    @Test
    fun twoPointsSlopingDown() {
        val a = XYDataPoint(0.0, 0.0)
        val b = XYDataPoint(1.0, -1.0)
        val series = XYDataSet(arrayOf(a, b))

        val line = series.lineOfBestFit()

        assertEquals(0.0, line.b, MARGIN)
        assertEquals(-1.0, line.m, MARGIN)
    }

    @Test
    fun threePointsSlopingUp() {
        val a = XYDataPoint(0.0, 0.0)
        val b = XYDataPoint(1.0, 1.0)
        val c = XYDataPoint(2.0, 2.0)

        val series = XYDataSet(arrayOf(a, b, c))
        val line = series.lineOfBestFit()

        assertEquals(0.0, line.b, MARGIN)
        assertEquals(1.0, line.m, MARGIN)
    }

    @Test
    fun complexExample1() {
        // From varsitytutors.com
        val series = XYDataSet(
            arrayOf(
                XYDataPoint(8.0, 3.0),
                XYDataPoint(2.0, 10.0),
                XYDataPoint(11.0, 3.0),
                XYDataPoint(6.0, 6.0),
                XYDataPoint(5.0, 8.0),
                XYDataPoint(4.0, 12.0),
                XYDataPoint(12.0, 1.0),
                XYDataPoint(9.0, 4.0),
                XYDataPoint(6.0, 9.0),
                XYDataPoint(1.0, 14.0)
            )
        )
        val line = series.lineOfBestFit()

        assertEquals(-1.1064189, line.m, MARGIN)
        assertEquals(14.08108, line.b, MARGIN)
    }

    @Test
    fun complexExample2() {
        // From mathbits.com
        val series = XYDataSet( arrayOf(
            XYDataPoint(9.0, 260.0),
            XYDataPoint(13.0, 320.0),
            XYDataPoint(21.0, 420.0),
            XYDataPoint(30.0, 530.0),
            XYDataPoint(31.0, 560.0),
            XYDataPoint(31.0, 550.0),
            XYDataPoint(34.0, 590.0),
            XYDataPoint(25.0, 500.0),
            XYDataPoint(28.0, 560.0),
            XYDataPoint(20.0, 440.0),
            XYDataPoint(5.0, 300.0)
        ))
        val line = series.lineOfBestFit()

        assertEquals(193.8521, line.b, MARGIN)
        assertEquals(11.7312, line.m, MARGIN)
    }


    @Test
    fun twoPointsOnTopOfEachOther() {
        val series = XYDataSet(
            arrayOf(
                XYDataPoint(0.0, 0.0),
                XYDataPoint(0.0, 0.0)
            )
        )
        val line = series.lineOfBestFit()
        assertTrue(line.b.isNaN())
        assertTrue(line.m.isNaN())
    }

    @Test
    fun horizontalLine() {
        val series = XYDataSet(arrayOf(
            XYDataPoint(0.0, 0.0),
            XYDataPoint(1.0, 0.0)
        ))
        val line = series.lineOfBestFit()

        assertEquals(0.0, line.b, MARGIN)
        assertEquals(0.0, line.m, MARGIN) // Gradient = vert / horiz = 0 / 1 = 0
    }

    @Test
    fun verticalLine() {
        val series = XYDataSet(arrayOf(
            XYDataPoint(0.0, 0.0),
            XYDataPoint(0.0, 1.0)
        ))
        val line= series.lineOfBestFit()
        assertTrue(line.b.isNaN()) // follows Y axis
        assertTrue(line.m.isNaN()) // Gradient = v / h = 1 / 0 = Nan
    }

    @Test
    fun noDataPoints() {
        val series = XYDataSet(emptyArray())
        val line = series.lineOfBestFit()
        assertTrue(line.b.isNaN())
        assertTrue(line.m.isNaN())
    }

    @Test
    fun oneDataPoint() {
        val series = XYDataSet(arrayOf(XYDataPoint(0.0, 0.0)))
        val line = series.lineOfBestFit()
        assertTrue(line.b.isNaN())
        assertTrue(line.m.isNaN())
    }



    companion object {
        const val MARGIN = 0.0001
    }

}