package com.aragang.chipiolo.views

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.TabViewModel

@Composable
fun Statistics(viewModel: TabViewModel) {

    val info_statis = listOf("Partidas Jugadas", "Partidas Ganadas", "Chipiolos", "amigos")
    val val_statis = mapOf("Partidas Jugadas" to "16","Partidas Ganadas" to "15","Chipiolos" to "0","amigos" to "2")


    Column(modifier = Modifier
        .fillMaxSize()
        .padding()
        .draggable(
            state = viewModel.dragState.value!!,
            orientation = Orientation.Horizontal,
            onDragStarted = { },
            onDragStopped = {
                viewModel.updateTabIndexBasedOnSwipe()
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {

            items(info_statis) {item ->
                Card(
                    modifier = Modifier.padding(4.dp).width(100.dp).height(90.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)) {

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                    ) {
                        androidx.compose.material.Text(
                            text = item,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                            fontStyle = FontStyle.Italic
                        )
                        androidx.compose.material.Text(
                            text = val_statis[item]!!,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    /*BoxWithConstraints {
                        if (maxWidth < 40.dp){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                modifier = Modifier
                            ) {
                                Text(
                                    text = item,
                                    textAlign = TextAlign.Center,
                                    fontSize = 7.sp,
                                )
                                Text(
                                    text = item,
                                    textAlign = TextAlign.Center,
                                    fontSize = 7.sp,
                                )
                            }
                        } else{
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                modifier = Modifier
                            ) {
                                Text(
                                    text = item,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp,
                                )
                                Text(
                                    text = item,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp,
                                )
                            }
                        }
                    }*/
                }
            }
        }
    }
}