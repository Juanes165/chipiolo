package com.aragang.chipiolo.Profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aragang.chipiolo.R

class Xd: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Juju()
        }
    }
}

@Composable
fun ProfileHome() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(43, 168, 74))) {

        Image(
            painter = painterResource(R.drawable.logo_chipiolo),
            contentDescription = "Logo de fondo",
            colorFilter = ColorFilter.tint(Color(43, 168, 74), blendMode = BlendMode.Darken),
                    modifier = Modifier
                .padding(
                    start = 150.dp
                )
                .size(250.dp)
        )

        Column(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 40.dp
                )
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

        }

        /*Image(
            painter = painterResource(R.drawable.rectangle),
            contentDescription = "Rectangle"
        )*/

    }
}

@Preview
@Composable
fun Juju() {
    ProfileHome()
}