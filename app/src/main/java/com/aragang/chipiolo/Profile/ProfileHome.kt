package com.aragang.chipiolo.Profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R
import com.aragang.chipiolo.TabViewModel
import com.aragang.chipiolo.views.Achievements
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.aragang.chipiolo.SignInChipiolo.UserData
import com.aragang.chipiolo.views.Statistics

/*
class Xd: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileHome()
        }
    }
}
*/

@Composable
fun ProfileHome(
    viewModel: TabViewModel,
    userData: UserData?,
    onSignOut: () -> Unit) {

    var showDialog by remember { mutableStateOf(false) }
    fun exit() {
        showDialog = false
        onSignOut
    }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 40.dp
                )
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
                .height(255.dp)) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 280.dp, height = 150.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 100.dp, topEnd = 50.dp)
                        )
                        .align(Alignment.Center)
                ) {
                    if (userData?.name != null) {
                        Text(
                            text = userData?.name!!,
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }else {
                        Text(
                            text = "",
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }

                }
                if (userData?.profileImage != null) {
                    AsyncImage(
                        model = userData.profileImage,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(4.dp, Color.White),
                                CircleShape
                            )
                            .padding(4.dp)
                            .clip(CircleShape)
                            .align(Alignment.TopCenter)
                            .clickable { showDialog = true },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.juanes_prueba),
                        contentDescription = "Image for profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .border(
                                BorderStroke(4.dp, Color.White),
                                CircleShape
                            )
                            .padding(4.dp)
                            .clip(CircleShape)
                            .align(Alignment.TopCenter)
                    )
                }

                Row(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(280.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    ButtonStatistics(
                        Modifier.padding(end = 1.dp),
                        "Estadistica",
                        RoundedCornerShape(bottomStart = 30.dp))
                    ButtonStatistics(
                        Modifier.padding(start = 1.dp),
                        "logros",
                        RoundedCornerShape(bottomEnd = 30.dp))
                }
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Tabs(viewModel = viewModel)

            if (showDialog) {
                Dialog(onDismissRequest = {showDialog = false}) {
                    // Custom shape, background, and layout for the dialog
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("¿Deseas Salir?")

                            Row {
                                Button(
                                    onClick = onSignOut,
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .width(115.dp)
                                ) {
                                    Text("Salir", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {showDialog = false},
                                    modifier = Modifier
                                        .padding(top = 16.dp, start = 5.dp)
                                        .width(115.dp)
                                ) {
                                    Text("Cancelar", fontSize = 11.sp)
                                }

                            }

                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ButtonStatistics(
    modi: Modifier,
    text: String,
    roundC: RoundedCornerShape) {

    var enabled by remember {mutableStateOf(false)}
    var enable_button = Color.Gray
    var disabled_button = Color.White

    Box(
        modifier = modi
            .background(
                color = if (enabled) enable_button else disabled_button,
                shape = roundC
            )
            .clip(roundC)
            .size(width = 140.dp, height = 50.dp)
            .clickable(enabled = enabled) {
                enabled = true
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (enabled) Color.White else Color.Black.copy(alpha = 0.4f),
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun Tabs(viewModel: TabViewModel) {
    val tabI = viewModel.tabIndex.observeAsState()

    when (tabI.value) {
        0 -> Statistics(viewModel = viewModel)
        1 -> Achievements(viewModel = viewModel)
    }
}


@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle = MaterialTheme.typography.body1,
    modifier: Modifier = Modifier,
    color: Color = style.color
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.body1.fontSize

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        }
    )
}

/*
@Preview
@Composable
fun Juju() {
    ProfileHome()
}*/
