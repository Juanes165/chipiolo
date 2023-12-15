package com.aragang.chipiolo

import androidx.annotation.DrawableRes

data class Card(
    val id: Int,
    val value: Int,
    @DrawableRes val imageRes: Int,

)