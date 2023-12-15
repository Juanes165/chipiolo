package com.aragang.chipiolo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@Composable
fun ResultsScreen(
    bot1: Bot,
    bot2: Bot,
    bot3: Bot,
    userCards: List<Card>,
    plantedPlayer: Int,
    goToMenu: () -> Unit = { }
) {

    println("bot1 cards: ${bot1.cards}")
    println("bot2 cards: ${bot2.cards}")
    println("bot3 cards: ${bot3.cards}")
    println("user cards: $userCards")

    // Puntaje inicial del aragang
    var userScore = getUserScores(userCards) as MutableList<Int>
    println("userScore: $userScore")

    // puntajes de los bots
    var bot1Score = getUserScores(bot1.cards) as MutableList<Int>
    var bot2Score = getUserScores(bot2.cards) as MutableList<Int>
    var bot3Score = getUserScores(bot3.cards) as MutableList<Int>

    // quien se planta
    var planted = ""
    when (plantedPlayer) {
        1 -> planted = "Te has plantado"
        2 -> planted = "${bot1.name} se ha plantado"
        3 -> planted = "${bot2.name} se ha plantado"
        4 -> planted = "${bot3.name} se ha plantado"
    }

    var winnerScore = 0
    var winnerName = ""
    when (plantedPlayer) {
        1 -> winnerScore = userScore.max()
        2 -> winnerScore = bot1Score.max()
        3 -> winnerScore = bot2Score.max()
        4 -> winnerScore = bot3Score.max()
    }

    when (plantedPlayer) {
        1 -> winnerName = "Has ganado"
        2 -> winnerName = "${bot1.name} ha ganado"
        3 -> winnerName = "${bot2.name} ha ganado"
        4 -> winnerName = "${bot3.name} ha ganado"
    }

    // ================================== GANADOR =========================================


    val colorWhite = colorResource(id = R.color.white)
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorGreenSecondary = colorResource(id = R.color.green_secondary)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)


    // show dialog
    var showDialog by remember { mutableStateOf(true) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
            .padding(10.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(colorGreenPrimary)
                .padding(horizontal = 0.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = planted,
                color = colorWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                fontSize = 32.sp,
            )
            PlayerHand(bot1.cards, "Bot1", planted = plantedPlayer == 2)
            PlayerHand(bot2.cards, "Bot2", planted = plantedPlayer == 3)
            PlayerHand(bot3.cards, "Bot3", planted = plantedPlayer == 4)
        }

        // ================================== PUNTUACION =========================================

        var max = 0
        for (i in 0..3) {
            if (userScore[i] > max) {
                max = userScore[i]
            }
        }
        val maxPoints = max

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 300.dp)
                .offset(0.dp, (-20).dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0..3) {
                val suit = when (i) {
                    0 -> R.drawable.logo_trebol
                    1 -> R.drawable.logo_diamante
                    2 -> R.drawable.logo_corazon
                    else -> R.drawable.logo_pica
                }
                if (userScore[i] == maxPoints) {
                    SuitPoints(suit = suit, points = maxPoints)
                }
            }
        }

        // ============================ CARTAS DEL JUGADOR ======================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
                .align(Alignment.BottomCenter)
                .zIndex(10f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, (-20).dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // CARTA 1 (IZQUIERDA)
                Image(
                    painter = painterResource(id = userCards[0].imageRes),
                    contentDescription = "Card 3",
                    modifier = Modifier
                        //.align(Alignment.BottomCenter)
                        .size(200.dp)
                        .offset((-40).dp, (10).dp)
                        .padding(horizontal = 30.dp)
                        .zIndex(1f)
                        .graphicsLayer {
                            this.transformOrigin = transformOrigin
                            rotationZ = -10f
                        }
                )

                // CARTA 2 (CENTRO)
                Image(
                    painter = painterResource(id = userCards[1].imageRes),
                    contentDescription = "Card 1",
                    modifier = Modifier
                        .size(200.dp)
                        .zIndex(2f)
                        .padding(horizontal = 30.dp)
                )

                // CARTA 3 (DERECHA)
                Image(
                    painter = painterResource(id = userCards[2].imageRes),
                    contentDescription = "Card 2",
                    modifier = Modifier
                        .size(200.dp)
                        .offset(40.dp, (10).dp)
                        .padding(horizontal = 30.dp)
                        .zIndex(3f)
                        .graphicsLayer {
                            this.transformOrigin = transformOrigin
                            rotationZ = 10f
                        }
                )

                // CARTA 4 VOLTEADA (OPCIONAL)
                if (plantedPlayer != 1) {
                    Image(
                        painter = painterResource(id = R.drawable.card_back),
                        contentDescription = "Card 2",
                        modifier = Modifier
                            .size(200.dp)
                            .offset(80.dp, (30).dp)
                            .padding(horizontal = 30.dp)
                            .zIndex(3f)
                            .graphicsLayer {
                                this.transformOrigin = transformOrigin
                                rotationZ = 20f
                            }
                    )
                }
            }
        }
        // ================================ BOTONES DE ABAJO ======================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    goToMenu()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorGreenPrimary,
                    contentColor = colorWhite
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(
                    bottom = 15.dp,
                    top = 15.dp,
                    start = 30.dp,
                    end = 30.dp
                )
            ) {
                Text(
                    text = "Volver a jugar",
                    color = colorWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }

    if (showDialog) {
        WinnerDialog(winner = winnerName, score = winnerScore, dismissRequest = { showDialog = false })
    }
}

@Composable
fun PlayerHand(
    playerCards: List<Card>,
    name: String = "Bot",
    planted: Boolean = false
) {

    val playerScore = getUserScores(playerCards) as MutableList<Int>
    var max = 0
    for (i in 0..3) {
        if (playerScore[i] > max) {
            max = playerScore[i]
        }
    }
    val maxPoints = max

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .horizontalScroll(rememberScrollState(), false),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = name,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(16.dp)
            )

            // CARTA 1 (IZQUIERDA)
            Image(
                painter = painterResource(id = playerCards[0].imageRes),
                contentDescription = "Card 1",
                modifier = Modifier
                    //.align(Alignment.BottomCenter)
                    .size(75.dp)
                    .zIndex(1f)
            )

            // CARTA 2 (CENTRO)
            Image(
                painter = painterResource(id = playerCards[1].imageRes),
                contentDescription = "Card 2",
                modifier = Modifier
                    .size(75.dp)
                    .offset(-(50).dp, (0).dp)
                    .zIndex(2f)
            )

            // CARTA 3 (DERECHA)
            Image(
                painter = painterResource(id = playerCards[2].imageRes),
                contentDescription = "Card 3",
                modifier = Modifier
                    .size(75.dp)
                    .offset(-(100).dp, (0).dp)
                    .zIndex(3f)
            )

            if (!planted) {
                // CARTA 4 VOLTEADA (OPCIONAL)
                Image(
                    painter = painterResource(id = playerCards[3].imageRes),
                    contentDescription = "Card 4",
                    modifier = Modifier
                        .size(75.dp)
                        .offset(-(150).dp, (0).dp)
                        .zIndex(4f)
                )
            }

            // ================================== PUNTUACION =========================================
            for (i in 0..3) {
                val suit = when (i) {
                    0 -> R.drawable.logo_trebol
                    1 -> R.drawable.logo_diamante
                    2 -> R.drawable.logo_corazon
                    else -> R.drawable.logo_pica
                }
                if (playerScore[i] == maxPoints) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .offset(-(150).dp, (0).dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SuitPoints(suit = suit, points = maxPoints)
                    }

//                    Image(
//                        painter = painterResource(suit),
//                        contentDescription = "palo",
//                        modifier = Modifier
//                            .size(60.dp)
//                            .padding(horizontal = 15.dp, vertical = 5.dp)
//                            .offset(-(160).dp, (-10).dp)
//                    )
//                    Text(
//                        text = "$maxPoints",
//                        modifier = Modifier
//                            .padding(end = 15.dp, top = 5.dp, bottom = 5.dp)
//                            .offset(-(160).dp, (-10).dp),
//                        color = Color.White,
//                        fontSize = 32.sp,
//                        fontWeight = FontWeight.Bold
//                    )
                }
            }
        }
    }
}


@Composable
fun WinnerDialog(winner: String, score: Int, dismissRequest: () -> Unit = { }) {
    val colorWhite = colorResource(id = R.color.white)
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorGreenSecondary = colorResource(id = R.color.green_secondary)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)

    Dialog(
        onDismissRequest = { dismissRequest() },
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, Color(0, 0, 0, 255)),
                    RoundedCornerShape(16.dp)
                )
            //.background(Color.Green)
        ) {

            Column(
                modifier = Modifier
                    //.background(colorGreenPrimary)
                    .padding(horizontal = 0.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = winner,
                    color = colorWhite,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 32.sp,
                )
                Text(
                    text = "Puntos: $score",
                    color = colorWhite,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 32.sp,
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            dismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorGreenPrimary,
                            contentColor = colorWhite
                        ),
                        shape = MaterialTheme.shapes.medium,
                        contentPadding = PaddingValues(
                            bottom = 15.dp,
                            top = 15.dp,
                            start = 30.dp,
                            end = 30.dp
                        )
                    ) {
                        Text(
                            text = "Ver Resultados",
                            color = colorWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}



