package com.aragang.chipiolo

import androidx.compose.ui.geometry.Offset
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsKtTest {
    @Test
    fun calculateDistanceBetweenTwoPointsPositive() {
        assertEquals(5f, calculateDistanceBetweenTwoPoints(0f, 0f, 3f, 4f))
    }

    @Test
    fun calculateDistanceBetweenTwoPointsNegative() {
        assertEquals(5f, calculateDistanceBetweenTwoPoints(0f, 0f, -3f, -4f))
    }

    @Test
    fun calculateDistanceBetweenTwoPointsOffsetPositive() {
        assertEquals(5f, calculateDistanceBetweenTwoPoints(Offset(0f, 0f), Offset(3f, 4f)))
    }

    @Test
    fun calculateDistanceBetweenTwoPointsOffsetNegative() {
        assertEquals(5f, calculateDistanceBetweenTwoPoints(Offset(0f, 0f), Offset(-3f, -4f)))
    }
}