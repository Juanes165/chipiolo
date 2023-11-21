package com.aragang.chipiolo.Profile

import android.Manifest
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
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
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.aragang.chipiolo.SignInChipiolo.UserData
import com.aragang.chipiolo.views.Statistics

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import java.io.File
import java.util.concurrent.Executor


@Composable
fun ProfileHome(
    viewModel: TabViewModel,
    userData: UserData?,
    onSignOut: () -> Unit,
    onPlay: () -> Unit,
    onProfile: () -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(43, 168, 74))
    ) {

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
            Box(
                modifier = Modifier
                    .padding(top = 100.dp)
                    .fillMaxWidth()
                    .height(255.dp)
                    .background(Color(66, 79, 88), RoundedCornerShape(30.dp))
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 280.dp, height = 150.dp)
                        .align(Alignment.Center),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(255, 255, 255),
                    ),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ) {
                    if (userData?.name != null) {
                        Text(
                            text = userData.name,
                            modifier = Modifier
                                .padding(top = 50.dp, start = 20.dp, end = 20.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    } else {
                        Text(
                            text = "",
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }

                    Button(
                        onClick = onPlay,
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.CenterHorizontally),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.play_icon),
                            contentDescription = "Play Button",
                            modifier = Modifier
                                .size(80.dp)
                                .fillMaxSize()
                        )
                    }

                }

                // FOTO DE PERFIL
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
                            .clickable { onProfile() },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.juanes_prueba),
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
                        "Estadistica",
                        RoundedCornerShape(bottomStart = 30.dp)
                    )
                    ButtonStatistics(
                        Modifier.padding(start = 1.dp),
                        "logros",
                        RoundedCornerShape(bottomEnd = 30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Tabs(viewModel = viewModel)

            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    // Custom shape, background, and layout for the dialog
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        if (userData != null) {
                            ProfilePopup(
                                userData = userData,
                                onSignOut = onSignOut,
                                updateShowDialog = { showDialog = false }
                            )
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
    roundC: RoundedCornerShape
) {

    var enabled by remember { mutableStateOf(false) }
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
fun ProfilePopup(userData: UserData, onSignOut: () -> Unit, updateShowDialog: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // FOTO DE PERFIL
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
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.juanes_prueba),
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
            )
        }

        if (userData != null) {
            Text(
                text = userData.name ?: "",
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = userData.email ?: "",
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Text(
            text = "Cambiar foto de perfil",
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Divider(
            color = Color.Red,
            thickness = 2.dp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Cerrar sesión",
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Red
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


//@Preview
//@Composable
//fun Juju() {
//    ProfileHome(null, null, {}, {})
//}
