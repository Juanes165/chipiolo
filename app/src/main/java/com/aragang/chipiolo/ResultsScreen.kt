package com.aragang.chipiolo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ResultsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0, 97, 23, 255)
            )
    ){
        Column {
            Text(text = "Results Screen")
            PlayerHand()
            PlayerHand()
            PlayerHand()
            PlayerHand()
        }
    }
}

@Composable
fun PlayerHand(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0, 97, 23, 255)
            )
    ){
        Column {
            Text(text = "Player Hand")

        }
    }

}


@Preview
@Composable
fun ResultsScreenPreview() {
    ResultsScreen()
}