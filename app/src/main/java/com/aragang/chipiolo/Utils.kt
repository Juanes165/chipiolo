package com.aragang.chipiolo

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt

fun calculateDistanceBetweenTwoPoints(p1: Offset, p2: Offset): Float {
    return calculateDistanceBetweenTwoPoints(p1.x, p1.y, p2.x, p2.y)
}

fun calculateDistanceBetweenTwoPoints(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    return sqrt((x2 - x1).toDouble().pow(2.0) + (y2 - y1).toDouble().pow(2.0)).toFloat()
}

fun returnImageResource(image: String): Int {
    when(image)
    {
        "avatar1" -> return R.drawable.avatar1
        "avatar2" -> return R.drawable.avatar2
        "avatar3" -> return R.drawable.avatar3
        "avatar4" -> return R.drawable.avatar4
        "avatar5" -> return R.drawable.avatar5
        "avatar6" -> return R.drawable.avatar6
        "avatar7" -> return R.drawable.avatar7
        "avatar8" -> return R.drawable.avatar8
        "avatar9" -> return R.drawable.avatar9
        else -> return R.drawable.avatar0
    }
}

val avatarList = listOf(
    "avatar0",
    "avatar1",
    "avatar2",
    "avatar3",
    "avatar4",
    "avatar5",
    "avatar6",
    "avatar7",
    "avatar8",
    "avatar9"
)