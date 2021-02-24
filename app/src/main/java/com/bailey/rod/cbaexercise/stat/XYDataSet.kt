package com.bailey.rod.cbaexercise.stat

/**
 * Series of points where the x value is monotonically increasing.
 */
class XYDataSet(private val series : Array<XYDataPoint>) {

    /**
     * The line y = mx + b
     *
     * @property m Gradient of the line
     * @property b Y intercept of the line
     */
    data class Line(val m : Double, val b : Double)

    /**
     * For line of best fit y = mx + b, find m and b:
     *
     * m = SUM (x[i] - av_x) * (y[i] - av_y) / SUM (x[i] - av_x) ^ 2
     * b = av_y - m * av_x
     *
     * av_x = average of x
     * av_y = average of y
     * m = gradient of line of best fit
     * b = y intercept of line of best fit
     */
    fun lineOfBestFit() : Line {
        val av_x : Double = findAverageX()
        val av_y : Double = findAverageY()

        var numeratorSum = 0.0
        var denominatorSum = 0.0

        for (point in series) {
            val xdiff = point.x - av_x
            val ydiff = point.y - av_y
            numeratorSum += (xdiff * ydiff)
            denominatorSum += xdiff * xdiff
        }

        val m = numeratorSum / denominatorSum
        val b = av_y - (m * av_x)

        return Line(m, b)
    }

    private fun findAverageX() : Double {
        var sum  = 0.0
        for (point in series) {
            sum += point.x
        }
        return sum / series.size
    }

    private fun findAverageY() : Double {
        var sum  = 0.0
        for (point in series) {
            sum += point.y
        }
        return sum / series.size
    }

}