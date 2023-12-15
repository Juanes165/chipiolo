package com.aragang.chipiolo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


@Composable
fun ResultsScreen(
    bot1cards: List<Card>,
    bot2cards: List<Card>,
    bot3cards: List<Card>,
    userCards: List<Card>,
    plantedPlayer: Int
) {

    println(userCards)

    // Puntaje inicial del aragang
    var userScore = getUserScores(userCards) as MutableList<Int>
    println("userScore: $userScore")

    // puntajes de los bots
    var bot1Score = getUserScores(bot1cards) as MutableList<Int>
    var bot2Score = getUserScores(bot2cards) as MutableList<Int>
    var bot3Score = getUserScores(bot3cards) as MutableList<Int>


    val colorWhite = colorResource(id = R.color.white)
    val colorDarkGray = colorResource(id = R.color.dark_gray)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colorDarkGray)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Results Screen",
                color = colorWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                fontSize = 32.sp,
            )
            PlayerHand(bot1cards, plantedPlayer == 2)
            PlayerHand(bot2cards, plantedPlayer == 3)
            PlayerHand(bot3cards, plantedPlayer == 4)
        }

        // ================================== PUNTUACION =========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 300.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (userScore[0] != 0) {
                SuitPoints(suit = R.drawable.logo_trebol, points = userScore[0])
            }
            if (userScore[1] != 0) {
                SuitPoints(suit = R.drawable.logo_diamante, points = userScore[1])
            }
            if (userScore[2] != 0) {
                SuitPoints(suit = R.drawable.logo_corazon, points = userScore[2])
            }
            if (userScore[3] != 0) {
                SuitPoints(suit = R.drawable.logo_pica, points = userScore[3])
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
                    .fillMaxWidth(),
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
                if(plantedPlayer != 1){
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
    }

}

@Composable
fun PlayerHand(
    playerCards: List<Card>,
    planted: Boolean = false
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(25, 255, 0, 255)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
    ) {

        // CARTA 1 (IZQUIERDA)
        Image(
            painter = painterResource(id = playerCards[0].imageRes),
            contentDescription = "Card 1",
            modifier = Modifier
                //.align(Alignment.BottomCenter)
                .size(75.dp)
        )

        // CARTA 2 (CENTRO)
        Image(
            painter = painterResource(id = playerCards[1].imageRes),
            contentDescription = "Card 2",
            modifier = Modifier
                .size(75.dp)
        )

        // CARTA 3 (DERECHA)
        Image(
            painter = painterResource(id = playerCards[2].imageRes),
            contentDescription = "Card 3",
            modifier = Modifier
                .size(75.dp)
        )

        if (!planted){
            // CARTA 4 VOLTEADA (OPCIONAL)
            Image(
                painter = painterResource(id = R.drawable.card_back),
                contentDescription = "Card 4",
                modifier = Modifier
                    .size(75.dp)
            )
        }
    }


}



