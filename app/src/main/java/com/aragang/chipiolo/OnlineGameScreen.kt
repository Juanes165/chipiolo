package com.aragang.chipiolo

import android.app.ProgressDialog
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.aragang.chipiolo.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import com.aragang.chipiolo.CreateRoom
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.*

@Composable
fun OnlineGameScreen() {

    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)
    val focusManager = LocalFocusManager.current

    val playerName = remember{mutableStateOf("")}
    val roomId = remember{mutableStateOf("")}
    val loading = remember {
        mutableStateOf(false)
    }
    var player2UniqueId = "0"
    var player3UniqueId = "0"
    var player4UniqueId = "0"
    var lobbyExists = false
    var status = "waiting"
    var playerTurn = ""
    var connectionId = ""

    var turnsEventListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            for (dataSnapshot in snapshot.children){
                if (dataSnapshot.childrenCount.toInt()==4){
                    //Gameplay I guess
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }
    var winnerEventListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }


    val firebase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chipiolo-c6c7c-default-rtdb.firebaseio.com/")
    val player1UniqueId = System.currentTimeMillis().toString()
    fun lookForLobby(){
        firebase.child("connections").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!lobbyExists){

                    if(snapshot.hasChildren()){
                        for(connection in snapshot.children) {
                            var conId = connection.key
                            var getPlayerCount = connection.childrenCount.toInt()
                            if(roomId.value == snapshot.key.toString())
                            {
                                //Found matching room
                                if(getPlayerCount<4){
                                    connection.child(player1UniqueId).child("player_name").ref.setValue(playerName.value)
                                    for(player in connection.children){
                                        var player1Found = false
                                        var player2Found = false
                                        var player3Found = false
                                        if(!player1Found)
                                        {
                                            var opponentName = player.child("player_name").value
                                            player2UniqueId = player.key.toString()
                                            player1Found = true
                                        }
                                        if(player1Found){
                                            var opponentName = player.child("player_name").value
                                            player3UniqueId = player.key.toString()
                                            player2Found = true
                                        }
                                        if(player2Found){
                                            var opponentName = player.child("player_name").value
                                            player4UniqueId = player.key.toString()
                                            player3Found = true
                                        }
                                        playerTurn = player2UniqueId
                                        connectionId = conId.toString()
                                        firebase.child("turns").child(connectionId).addValueEventListener(turnsEventListener)
                                        firebase.child("winner").child(connectionId).addValueEventListener(winnerEventListener)
                                        if(loading.value) !loading.value
                                        firebase.child("connections").removeEventListener(this)

                                    }
                                    !lobbyExists
                                }
                            }
                            else{
                                if(getPlayerCount == 4){
                                    playerTurn = player1UniqueId
                                    var player1Found = false
                                    var player2Found = false
                                    var player3Found = false

                                    for (player in connection.children){
                                        var getPlayerUniqueId = player.key
                                        if (getPlayerUniqueId == player1UniqueId)
                                        {
                                            player1Found = true
                                        }
                                        else if (player1Found){
                                            var opponentName = player.child("player_name").value
                                            player2UniqueId = player.key.toString()
                                            player2Found = true
                                        }
                                        else if (player2Found){
                                            var opponentName = player.child("player_name").value
                                            player3UniqueId = player.key.toString()
                                            player3Found = true
                                        }
                                        else if (player3Found){
                                            var opponentName = player.child("player_name").value
                                            player4UniqueId = player.key.toString()
                                            connectionId = conId.toString()
                                            firebase.child("turns").child(connectionId).addValueEventListener(turnsEventListener)
                                            firebase.child("winner").child(connectionId).addValueEventListener(winnerEventListener)
                                            if(loading.value) !loading.value
                                            firebase.child("connections").removeEventListener(this)

                                        }
                                    }
                                    !lobbyExists
                                }
                            }
                        }

                    }
                    if(!lobbyExists && status=="waiting")
                    {
                        //val connectionUniqueId = System.currentTimeMillis().toString()
                        val connectionUniqueId = roomId.value

                        snapshot.child(connectionUniqueId).child(player1UniqueId).child("player_name").ref.setValue(playerName.value)
                        //snapshot.child(connectionUniqueId).child(player1UniqueId).child("player1_name").ref.setValue(playerName)
                        status = "waiting"
                        !lobbyExists
                    }

                }
                /*else{
                    val connectionUniqueId = System.currentTimeMillis().toString()
                    snapshot.child(connectionUniqueId).child(player1UniqueId).child("player1_name").ref.setValue(playerName.value)
                    status = "waiting"
                }*/
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        )
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
            .padding(start = 60.dp, end = 60.dp, top = 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = playerName.value,
            onValueChange = {playerName.value = it},
            label = { Text(text = stringResource(R.string.name_placeholder), fontSize = 16.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorWhite,
                unfocusedBorderColor = colorLightGray,
                focusedLabelColor = colorWhite,
                unfocusedLabelColor = colorLightGray,
                cursorColor = colorWhite,
                focusedTextColor = colorWhite,
                unfocusedTextColor = colorLightGray,
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,)

        OutlinedTextField(
            value = roomId.value,
            onValueChange = {roomId.value = it},
            label = { Text(text = stringResource(R.string.room_placeholder), fontSize = 16.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorWhite,
                unfocusedBorderColor = colorLightGray,
                focusedLabelColor = colorWhite,
                unfocusedLabelColor = colorLightGray,
                cursorColor = colorWhite,
                focusedTextColor = colorWhite,
                unfocusedTextColor = colorLightGray,
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,)
        Button(onClick = {
            loading.value=true
            lookForLobby()

            /*val database = Firebase.database
            val myRef = database.getReference("message")*/

            //firebase.child("tests").setValue("Hello, World!")
        },
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorGreenPrimary,
                contentColor = colorWhite
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(bottom = 15.dp, top = 15.dp)) {
            Text(text = "Unirse/Crear partida")
        }
        if (!loading.value) return
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }



    /*val context = LocalContext.current
    val density = LocalDensity.current

    val cards = remember {
        mutableStateOf(Data.cardList)
    }
    val cardsSpreadDegree = remember {
        mutableStateOf(10f)
    }
    val activeCard = remember {
        mutableStateOf<Card?>(null)
    }
    val droppedCards = remember {
        mutableListOf<Card>()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF266B35)
            )
    ) {

        Data.cardList.indices.forEach { index ->
            CardItem(
                card = Card(id = index, imageRes = R.drawable.card_back),
                index = index,
                nonDroppedCardsSize = cards.value.size,
                transformOrigin = TransformOrigin(1f, 0f),
                enableDrag = false,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = (-60).dp, y = 100.dp)
            )
        }

        cards.value.forEachIndexed { index, card ->
            key(card.id) {
                CardItem(
                    card = card,
                    index = index,
                    transformOrigin = TransformOrigin(0f, 1f),
                    nonDroppedCardsSize = cards.value.size - droppedCards.size,
                    activeCard = activeCard.value,
                    cardsSpreadDegree = cardsSpreadDegree.value,
                    isDropped = droppedCards.contains(card),
                    onCardDropped = { droppedCard ->
                        droppedCards.add(droppedCard)
                    },
                    setActiveCard = { activeCard.value = it },
                    getTargetOffset = {
                        val width = 50f * droppedCards.size
                        Offset(
                            x = width,
                            y = with(density) { maxHeight.toPx() / 2 } - 450f,
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(x = 60.dp, y = (-100).dp)
                        .then(
                            if (droppedCards.contains(card)) {
                                Modifier
                                    .zIndex(
                                        droppedCards
                                            .indexOf(card)
                                            .toFloat()
                                    )
                            } else {
                                Modifier
                                    .zIndex((droppedCards.size + index).toFloat())
                            }
                        )
                )
            }
        }

        PlayerHand(
            cardsSpreadDegree = cardsSpreadDegree,
            onHandDragged = { delta ->
                val newCardsSpreadDegree = max(
                    0f,
                    min(
                        12f,
                        cardsSpreadDegree.value + delta / 10f
                    )
                )
                cardsSpreadDegree.value = newCardsSpreadDegree
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 20.dp, y = (-10).dp)
                .zIndex((droppedCards.size + cards.value.size).toFloat())
        )

    }*/

}

@Composable
fun CreateParty(){



}

/*
@Composable
fun CardItem(
    card: Card,
    index: Int,
    transformOrigin: TransformOrigin,
    modifier: Modifier = Modifier,
    nonDroppedCardsSize: Int = 0,
    isDropped: Boolean = false,
    onCardDropped: (Card) -> Unit = {},
    getTargetOffset: () -> Offset = { Offset.Zero },
    enableDrag: Boolean = true,
    activeCard: Card? = null,
    setActiveCard: (Card?) -> Unit = {},
    cardsSpreadDegree: Float = 10f,
) {
    val scope = rememberCoroutineScope()
    val isBeingDragged = remember {
        mutableStateOf(false)
    }
    val activeCardOffset = animateFloatAsState(
        targetValue = if (activeCard == card && !isBeingDragged.value) -100f else 0f,
        label = "Active card ${card.id} offset animation"
    )
    val cardRotation = animateFloatAsState(
        targetValue = if (isDropped) 0f else cardsSpreadDegree * (index - nonDroppedCardsSize / 2) - 30f,
        label = "Card ${card.id} rotation animation"
    )
    val cardDropRotation = animateFloatAsState(
        targetValue = if (isDropped) 160f else 0f,
        label = "Card ${card.id} drop rotation animation",
        animationSpec = tween(
            durationMillis = 400,
            easing = EaseInOut,
        )
    )
    val cardDragX = remember {
        Animatable(initialValue = 0f,)
    }
    val cardDragY = remember {
        Animatable(initialValue = 0f,)
    }
    val cardOriginalOffset = remember {
        mutableStateOf(Offset.Zero)
    }

    Image(
        painter = painterResource(id = card.imageRes),
        contentDescription = "Card ${card.id}",
        modifier = modifier
            .width(120.dp)
            .wrapContentHeight()
            .onGloballyPositioned {
                cardOriginalOffset.value = it.positionInRoot() - Offset(
                    x = it.size.width / 2f,
                    y = it.size.height / 2f,
                )
            }
            .graphicsLayer {
                this.transformOrigin = transformOrigin
                rotationZ = cardRotation.value
            }
            .graphicsLayer {
                translationX = cardDragX.value
                translationY = activeCardOffset.value + cardDragY.value
            }
            .graphicsLayer {
                this.transformOrigin = TransformOrigin.Center
                rotationZ = cardDropRotation.value
            }
            .clip(MaterialTheme.shapes.small)
            .pointerInput(activeCard) {
                detectTapGestures(
                    onTap = {
                        if (isDropped) return@detectTapGestures
                        setActiveCard(
                            if (activeCard == card) null else card
                        )
                    },
                )
            }
            .then(
                if (enableDrag && !isDropped) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { startOffset ->
                                println("startOffset: $startOffset")
                                isBeingDragged.value = true
                                setActiveCard(card)
                            },
                            onDragEnd = {
                                isBeingDragged.value = false

                                val dragOffset = Offset(
                                    x = cardDragX.value,
                                    y = cardDragY.value,
                                )
                                val distance = calculateDistanceBetweenTwoPoints(
                                    dragOffset,
                                    Offset.Zero
                                )
                                val targetOffset = getTargetOffset()

                                println("targetOffset: $targetOffset")
                                println("originalOffset: ${cardOriginalOffset.value}")
                                println("drag offset: ${cardDragX.value}, ${cardDragY.value}")
                                println("Distance: $distance")

                                if (distance > 500) {
                                    val remainingOffset = targetOffset - cardOriginalOffset.value
                                    println("remainingOffset: $remainingOffset")
                                    scope.launch {
                                        cardDragX.animateTo(
                                            targetValue = remainingOffset.x,
                                            animationSpec = tween(
                                                durationMillis = 800,
                                                easing = EaseInOut
                                            )
                                        )
                                    }
                                    scope.launch {
                                        cardDragY.animateTo(
                                            targetValue = remainingOffset.y,
                                            animationSpec = tween(
                                                durationMillis = 800,
                                                easing = EaseInOut
                                            )
                                        )
                                    }
                                    onCardDropped(card)
                                } else {
                                    scope.launch {
                                        cardDragX.animateTo(0f)
                                    }
                                    scope.launch {
                                        cardDragY.animateTo(0f)
                                    }
                                }

                                setActiveCard(null)
                            },
                            onDragCancel = {
                                isBeingDragged.value = false
                                scope.launch {
                                    cardDragX.animateTo(0f)
                                }
                                scope.launch {
                                    cardDragY.animateTo(0f)
                                }
                                setActiveCard(null)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()

                                scope.launch {
                                    cardDragX.snapTo(cardDragX.value + dragAmount.x)
                                }
                                scope.launch {
                                    cardDragY.snapTo(cardDragY.value + dragAmount.y)
                                }
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
            .shadow(
                elevation = 10.dp,
                shape = MaterialTheme.shapes.small,
            )
            .then(
                if (activeCard == card) {
                    Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        )
                } else {
                    Modifier
                }
            )
    )
}

@Composable
fun PlayerHand(
    cardsSpreadDegree: State<Float>,
    onHandDragged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val isHandBeingDragged = remember {
        mutableStateOf(false)
    }

    Image(
        painter = painterResource(id = R.drawable.hand),
        contentDescription = "hand",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .width(100.dp)
            .wrapContentHeight()
            .graphicsLayer {
                transformOrigin = TransformOrigin(0f, 0f)
                rotationZ = cardsSpreadDegree.value - 10f
            }
            .draggable(
                state = rememberDraggableState { delta ->
                    onHandDragged(delta)
                },
                orientation = Orientation.Horizontal,
                onDragStarted = {
                    isHandBeingDragged.value = true
                },
                onDragStopped = {
                    isHandBeingDragged.value = false
                }
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    MyApplicationTheme {
        GameScreen()
    }
}*/
