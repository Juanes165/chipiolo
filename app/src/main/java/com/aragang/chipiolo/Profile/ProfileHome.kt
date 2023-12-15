package com.aragang.chipiolo.Profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Text
import androidx.compose.material3.Button
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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R
import com.aragang.chipiolo.TabViewModel
import com.aragang.chipiolo.views.Achievements
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.aragang.chipiolo.SignInChipiolo.UserData
import com.aragang.chipiolo.avatarList
import com.aragang.chipiolo.returnImageResource
import com.aragang.chipiolo.views.Statistics


@Composable
fun ProfileHome(
    viewModel: TabViewModel,
    userData: UserData?,
    onProfile: () -> Unit,
) {

    // Paleta de colores
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
    ) {

//        Image(
//            painter = painterResource(R.drawable.logo_chipiolo),
//            contentDescription = stringResource(R.string.logofondo),
//            colorFilter = ColorFilter.tint(Color(43, 168, 74), blendMode = BlendMode.Darken),
//            modifier = Modifier
//                .padding(
//                    start = 150.dp
//                )
//                .size(250.dp)
//        )

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

            // Titulo
            Text(
                text = stringResource(R.string.profile),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 80.dp, bottom = 40.dp),
                color = colorWhite
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp)
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 280.dp, height = 150.dp)
                        .align(Alignment.Center),
                    colors = CardDefaults.cardColors(
                        containerColor = colorWhite,
                    ),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ) {

                    // NOMBRE DE USUARIO
                    if (userData?.name != null) {
                        Text(
                            text = userData.name.split(" ").first().uppercase(),
                            modifier = Modifier
                                .padding(top = 85.dp, start = 20.dp, end = 20.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Nombre",
                            modifier = Modifier
                                .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

//                    Button(
//                        onClick = onPlay,
//                        shape = CircleShape,
//                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
//                        modifier = Modifier
//                            .size(80.dp)
//                            .align(Alignment.CenterHorizontally),
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.play_icon),
//                            contentDescription = stringResource(R.string.plybtn),
//                            modifier = Modifier
//                                .size(80.dp)
//                                .fillMaxSize()
//                        )
//                    }
                }

                // FOTO DE PERFIL
                if (userData?.profileImage != null) {
                    if (userData.profileImage !in avatarList) {
                        AsyncImage(
                            model = userData.profileImage,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(125.dp)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(5.dp, colorWhite),
                                    CircleShape
                                )
                                .padding(5.dp)
                                .clip(CircleShape)
                                .align(Alignment.TopCenter)
                                .background(Color.White)
                                .clickable { onProfile() },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val image = returnImageResource(userData.profileImage)
                        Image(
                            painter = painterResource(image),
                            contentDescription = "Default profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(125.dp)
                                .border(
                                    BorderStroke(4.dp, Color.White),
                                    CircleShape
                                )
                                .padding(4.dp)
                                .clip(CircleShape)
                                .align(Alignment.TopCenter)
                                .background(Color.White)
                                .clickable { onProfile() }
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(R.drawable.avatar0),
                        contentDescription = "Default profile picture",
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
                            .background(Color.White)
                            .clickable { onProfile() }
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(280.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ButtonStatistics(
                        Modifier.padding(end = 1.dp),
                        stringResource(R.string.std),
                        RoundedCornerShape(bottomStart = 30.dp)
                    )
                    ButtonStatistics(
                        Modifier.padding(start = 1.dp),
                        stringResource(R.string.lgr),
                        RoundedCornerShape(bottomEnd = 30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Tabs(viewModel = viewModel)
        }
    }
}


@Composable
fun ButtonStatistics(
    modifier: Modifier,
    text: String,
    roundC: RoundedCornerShape
) {

    var enabled by remember { mutableStateOf(false) }
    var enable_button = Color.Gray
    var disabled_button = Color.White

    Box(
        modifier = modifier
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
