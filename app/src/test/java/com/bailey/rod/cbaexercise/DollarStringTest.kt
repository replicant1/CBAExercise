package com.bailey.rod.cbaexercise

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Tests for the function that turns a value like 4.09 into a string like "$4.09"
 */
class DollarStringTest {
    @Test
    fun zeroDollars() {
        val str = 0F.getDollarString()
        assertEquals("$0.00", str)
    }

    @Test
    fun oneCent() {
        val str = 0.01F.getDollarString()
        assertEquals("$0.01", str)
    }

    @Test
    fun oneDollar() {
        val str = 1F.getDollarString()
        assertEquals("$1.00", str)
    }

    @Test
    fun oneDollarAndOneCent() {
        val str = 1.01F.getDollarString()
        assertEquals("$1.01", str)
    }

    @Test
    fun oneThousandDollarsAndTenCents() {
        val str = 1000.10F.getDollarString()
        assertEquals("$1,000.10", str)
    }

    @Test
    fun minusTenDollars() {
        val str = (-10F).getDollarString()
        assertEquals("$-10.00", str)
    }

    @Test
    fun minusOneHundredDollarsAndTwentyCents() {
        val str = (-100.20F).getDollarString()
        assertEquals("$-100.20", str)
    }

    @Test
    fun minusOneThousandDollarsAndFiveCents() {
        val str = (-1000.05F).getDollarString()
        assertEquals("$-1,000.05", str)
    }
}