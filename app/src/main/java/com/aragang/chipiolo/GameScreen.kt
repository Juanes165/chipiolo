package com.aragang.chipiolo

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.aragang.chipiolo.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Composable
fun GameScreen(
    goToMenu: () -> Unit = {}
) {

    // colores
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorGreenSecondary = colorResource(id = R.color.green_secondary)
    val colorRedPrimary = colorResource(id = R.color.red_primary)
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorWhite = colorResource(id = R.color.white)

    // mezclar las cartas y las guarda en una lista
    var allCards by remember {
        mutableStateOf(Data.cardList.shuffled())
    }

    // cartas que se van descartando
    var cardTrash by remember {
        mutableStateOf(listOf<Card>())
    }

    // si no hay cartas en la lista de cartas disponibles se vuelve a mezclar
    if (allCards.isEmpty()) {
        allCards = cardTrash.shuffled()
    }

    // bots
    var bot1cards by remember { mutableStateOf(allCards.take(3)) }
    var bot2cards by remember { mutableStateOf(allCards.drop(3).take(3)) }
    var bot3cards by remember { mutableStateOf(allCards.drop(6).take(3)) }

    // jugador
    var userCards by remember { mutableStateOf(allCards.drop(9).take(3)) }

    // delete card taks of the allCards list


    // Puntaje inicial del aragang
    var userScore = getUserScores(userCards) as MutableList<Int>

    // instancias para bots
    var bot1 = Bot("Bot1", bot1cards)
    var bot2 = Bot("Bot2", bot2cards)
    var bot3 = Bot("Bot3", bot3cards)

    // puntajes de los bots
    var bot1Score = getUserScores(bot1cards) as MutableList<Int>
    var bot2Score = getUserScores(bot2cards) as MutableList<Int>
    var bot3Score = getUserScores(bot3cards) as MutableList<Int>

    // cartas en la mesa
    var tableCard by remember { mutableStateOf(Card(53, 0, "", R.drawable.card_back)) }
    var droppedCard by remember { mutableStateOf(allCards.drop(12).take(1)[0]) }

    // NO SE POR QUE ESTA LINEA ME DA PROBLEMAS PERO SI LA PONGO NO FUNCIONA EL CODIGO
    // NO ENTIENDO KOTLIN ODIO MI VIDA ESTE ERROR ME DEMORE 1 HORA EN SOLUCIONAR
    // te quiero mucho geider
    // aqui se elimina las cartas asinadas de la lista de cartas disponibles
    //allCards = allCards.drop(3)
    val startedGame = remember { mutableStateOf(false) }
    if (!startedGame.value) {
        startedGame.value = true
        allCards = allCards.drop(13)
    }

    // VARIABLES IMPORTANTES PARA EL JUEGO
    // Turno
    var turn = remember { mutableStateOf(1) }

    // Se pidio carta
    var cardPicked by remember { mutableStateOf(false) }

    // Se bota una carda
    var cardDropped by remember { mutableStateOf(false) }

    // Se planta
    var canPlant by remember { mutableStateOf(canUserPlant(userScore)) }
    var planted by remember { mutableStateOf(false) }
    var giveCards by remember { mutableStateOf(false) }

    println("canPlant: $canPlant")
    println("cardsTrash: $cardTrash")

    // imprimir cartas pa tin
//    bot1.printCards()
//    bot2.printCards()
//    bot3.printCards()
//    println("user cards:")
//    userCards.forEach { card ->
//        println(card)
//    }

    // CAMBIOS DE TURNO
    when (turn.value) {
        1 -> {
            // Turno del usuario
            println("Turno del usuario")
            println(allCards.size)

        }

        2 -> {
            // Turno del bot 1
            var contador = 0
            val blockTime = Random.nextInt(1, 2)
            LaunchedEffect(turn.value) {
                while (contador < blockTime) {
                    delay(1000)
                    contador++
                }
                println("Turno del bot 1")
                //bot1.pickCard(allCards.toMutableList())


                val dropCardLessF = bot1.pickCard(allCards.toMutableList())
                allCards = allCards.drop(1)
                Log.e("Numero cartas Bot1", droppedCard.toString())

                if (dropCardLessF != null) {
                    cardTrash = cardTrash.plus(droppedCard)
                    droppedCard = dropCardLessF!!
                    Log.d("dropped card", dropCardLessF!!.toString())


                    println("KPOETA cards DESPUES :")
                    bot1.cards.forEach { card ->
                        println(card)
                    }


                } else {
                    Log.e("ME PLANTO", "KPEOTA SE PLANTO")
                }
                turn.value = 3
            }
        }

        3 -> {
            // Turno del bot 2

            var contador = 0
            val blockTime = Random.nextInt(1, 2)
            LaunchedEffect(turn.value) {
                while (contador < blockTime) {
                    delay(1000)
                    contador++
                }
                println("Turno del bot 2")
//            bot2.pickCard(allCards.toMutableList())
//            bot2.dropCard()
                val dropCardLessF = bot2.pickCard(allCards.toMutableList())
                allCards = allCards.drop(1)
                Log.e("Numero cartas Bot2", droppedCard.toString())

                if (dropCardLessF != null) {
                    cardTrash = cardTrash.plus(droppedCard)
                    droppedCard = dropCardLessF!!

                    Log.d("dropped card", dropCardLessF!!.toString())


                    println("SATORU cards DESPUES :")
                    bot1.cards.forEach { card ->
                        println(card)
                    }


                } else {
                    Log.d("ME PLANTO", "SATORU SE PLANTO")
                }
                turn.value = 4
            }
        }

        4 -> {
            // Turno del bot 3
            var contador = 0
            val blockTime = Random.nextInt(1, 2)
            LaunchedEffect(turn.value) {
                while (contador < blockTime) {
                    delay(1000)
                    contador++
                }
                println("Turno del bot 3")
//            bot3.pickCard(allCards.toMutableList())
//            bot3.dropCard()

                val dropCardLessF = bot3.pickCard(allCards.toMutableList())
                allCards = allCards.drop(1)
                Log.e("Numero cartas Bot3", droppedCard.toString())

                if (dropCardLessF != null) {
                    cardTrash = cardTrash.plus(droppedCard)
                    droppedCard = dropCardLessF!!
                    Log.d("dropped card", dropCardLessF!!.toString())


                    println("SATORU cards DESPUES :")
                    bot1.cards.forEach { card ->
                        println(card)
                    }

                    cardPicked = false
                    cardDropped = false

                } else {
                    Log.d("ME PLANTO", "SATORU SE PLANTO")
                }
                turn.value = 1

            }
        }
    }

    if (planted) {

        if (!giveCards) {
            println("planted")
            println("bot1 cards: ${bot1.cards}")
//            bot1cards = bot1.cards.plus(allCards[0])
//            bot2cards = bot2.cards.plus(allCards[1])
//            bot3cards = bot3.cards.plus(allCards[2])
//            allCards = allCards.drop(3)
            giveCards = true
        }
        ResultsScreen(
            bot1 = bot1,
            bot2 = bot2,
            bot3 = bot3,
            userCards = userCards,
            plantedPlayer = 1,
            goToMenu = goToMenu
        )
    } else {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFF266B35)
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                BotView(name = "KPEOTA", profilePic = R.drawable.avatar3)
                Column(
                    modifier = Modifier
                        .padding(bottom = 50.dp)

                ) {
                    BotView(name = "Salsatoru", profilePic = R.drawable.avatar4)
                }

                BotView(name = "Migueeeel", profilePic = R.drawable.avatar5)
            }


            // ================================== ANUNCIOS ======================================
            if (planted) {
                Text(
                    text = "Te plantaste",
                    color = Color.White,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 200.dp)
                )
            }
//        Text(
//            text = "Selecciona una carta",
//            color = Color.White,
//            fontSize = 32.sp,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 200.dp)
//        )


            // ============================ CARTA EN LA MESA ======================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 150.dp)
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(droppedCard.imageRes),
                    contentDescription = "Card back",
                    modifier = Modifier
                        .size(170.dp)
                        .padding(start = 40.dp)
                )
                Image(
                    painter = painterResource(tableCard.imageRes),
                    contentDescription = "Card back",
                    modifier = Modifier
                        .size(170.dp)
                        .padding(end = 40.dp)
                )
            }


            // ================================== PUNTUACION =========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 320.dp)
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
                    .padding(bottom = 100.dp)
                    .align(Alignment.BottomCenter),
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
                            .offset((-60).dp, (10).dp)
                            .padding(horizontal = 30.dp)
                            .zIndex(1f)
                            .graphicsLayer {
                                this.transformOrigin = transformOrigin
                                rotationZ = -10f
                            }
                            .clickable {
                                if (!cardDropped) {
                                    userCards = userCards.plus(droppedCard)
                                    droppedCard = userCards[0]
                                    userCards = userCards.drop(1)
                                    cardPicked = true
                                    userScore = getUserScores(userCards) as MutableList<Int>
                                    //turn.value = 2
                                }
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
                            .clickable {
                                if (!cardDropped) {
                                    userCards = userCards.plus(droppedCard)
                                    droppedCard = userCards[1]
                                    userCards = userCards
                                        .plus(userCards[0])
                                        .drop(2)
                                    cardPicked = true
                                    userScore = getUserScores(userCards) as MutableList<Int>
                                    //turn.value = 2
                                }
                            }
                    )

                    // CARTA 3 (DERECHA)
                    Image(
                        painter = painterResource(id = userCards[2].imageRes),
                        contentDescription = "Card 2",
                        modifier = Modifier
                            .size(200.dp)
                            .offset(60.dp, (10).dp)
                            .padding(horizontal = 30.dp)
                            .zIndex(3f)
                            .graphicsLayer {
                                this.transformOrigin = transformOrigin
                                rotationZ = 10f
                            }
                            .clickable {
                                if (!cardDropped) {
                                    userCards = userCards.plus(droppedCard)
                                    droppedCard = userCards[2]
                                    userCards = userCards
                                        .plus(userCards.take(2))
                                        .drop(3)
                                    cardPicked = true
                                    userScore = getUserScores(userCards) as MutableList<Int>
                                    //turn.value = 2
                                }
                            }
                    )

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
                if (!cardPicked) {

                    // =============== PEDIR CARTA ===============
                    Button(
                        onClick = {
                            cardTrash = cardTrash.plus(droppedCard)
                            droppedCard = allCards[0]
                            allCards = allCards.drop(1)
                            println(allCards.size)
                            cardPicked = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorDarkGray,
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
                            text = "Pedir carta",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // =============== PLANTARSE ===============
                    Button(
                        onClick = {
                            if (canPlant) {
                                planted = true
                                bot1cards = bot1cards.plus(allCards[0])
                                bot1Score = getUserScores(bot1cards) as MutableList<Int>

                                bot2cards = bot2cards.plus(allCards[1])
                                bot2Score = getUserScores(bot2cards) as MutableList<Int>

                                bot3cards = bot3cards.plus(allCards[2])
                                bot3Score = getUserScores(bot3cards) as MutableList<Int>
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorDarkGray,
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
                            text = "Plantarse", color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {

                    // =============== SEGUIR CON EL OTRO JUGADOR ===============
                    Button(
                        onClick = {
                            canPlant = canUserPlant(userScore)
                            turn.value = 2
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = colorDarkGray,
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
                            text = "Continuar", color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// retorna los puntajes del usuario de cada palo
// devuelve una lista de 4 elementos
// 0 es trebol, 1 es diamante, 2 es corazon, 3 es pica
// si hay un cero es que no tiene cartas de ese palo
fun getUserScores(
    cards: List<Card>
): List<Int> {
    var clubs = 0
    var diamonds = 0
    var hearts = 0
    var spades = 0

    cards.forEach { card ->
        when (card.suit) {
            "Clubs" -> clubs += card.value
            "Diamonds" -> diamonds += card.value
            "Hearts" -> hearts += card.value
            "Spades" -> spades += card.value
        }
    }

    val total = listOf(clubs, diamonds, hearts, spades)

    return total
}


// Ver si el jugador puede plantar
// solo se puede plantar si tiene 27 puntos o mas de un palo
fun canUserPlant(
    scores: List<Int>
): Boolean {
    var canPlant = false
    scores.forEach { score ->
        if (score >= 27) {
            canPlant = true
        }
    }
    if (scores.sum() == 33) canPlant = true

    return canPlant
}

// VISTA DEL BOT
@Composable
fun BotView(name: String, profilePic: Int) {
    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        Image(
            painter = painterResource(id = profilePic),
            contentDescription = "bot",
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
        )
    }
}


// Puntos del jugador
@Composable
fun SuitPoints(
    suit: Int,
    points: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.white)
            )
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.black),
                //shape = MaterialTheme.shapes.small
            )
            .padding(1.dp),
        //.clip(MaterialTheme.shapes.small)
        horizontalArrangement = Arrangement.Center,

        ) {
        Image(
            painter = painterResource(suit),
            contentDescription = "palo",
            modifier = Modifier
                .size(60.dp)
                .padding(horizontal = 15.dp, vertical = 5.dp)
        )
        Text(
            text = "$points",
            modifier = Modifier
                .padding(end = 20.dp, top = 5.dp, bottom = 5.dp),
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    MyApplicationTheme {
        GameScreen()
    }
}