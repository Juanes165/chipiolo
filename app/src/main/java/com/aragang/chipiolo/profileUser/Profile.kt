package com.aragang.chipiolo.profileUser

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.aragang.chipiolo.R
import com.aragang.chipiolo.SignInChipiolo.UserData


@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    onCamera: () -> Unit
) {

    var showSignOutDialog by remember { mutableStateOf(false) }
    var showProfilePictureDialog by remember { mutableStateOf(false) }

    var photoUri: Uri? by remember { mutableStateOf(null) }

    // Photo picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //When the user has selected a photo, its URL is returned here
        photoUri = uri
    }

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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 100.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Center,
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
                        .clip(CircleShape)
                        .clickable { showProfilePictureDialog = true },
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
                    text = userData.email,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Text(
                text = "Cambiar foto de perfil",
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Button(
            onClick = { showSignOutDialog = true },
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 0, 0),
                contentColor = Color.White,
            ),

            ) {
            Text(
                text = "Cerrar sesión",
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                color = Color.White
            )
        }

        if (showSignOutDialog) {
            SignOutDialog(
                signOut = onSignOut,
                closeDialog = { showSignOutDialog = false }
            )
        }

        if (showProfilePictureDialog) {
            ProfilePictureDialog(
                signOut = onSignOut,
                closeDialog = { showProfilePictureDialog = false },
                openCamera = onCamera,
                openGallery = {
                    launcher.launch(PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    ))
                }
            )
        }
    }
}


@Composable
fun ProfilePictureDialog(
    signOut: () -> Unit,
    openCamera: () -> Unit = {},
    openGallery: () -> Unit = {},
    closeDialog: () -> Unit = {}
) {
    Dialog(onDismissRequest = closeDialog) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, Color(0, 0, 0, 255)),
                    RoundedCornerShape(16.dp)
                )
        ) {
            Column {
                Text(
                    text = "Foto de perfil",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = openCamera,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 5.dp)
                        .width(115.dp)
                ) {
                    Text("Cámara", fontSize = 14.sp)
                }

                Button(
                    onClick = openGallery,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 5.dp)
                        .width(115.dp)
                ) {
                    Text("Galería", fontSize = 14.sp)
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 16.dp, start = 5.dp)
                        .width(115.dp)
                ) {
                    Text("Avatar", fontSize = 14.sp)
                }
            }
        }
    }
}


@Composable
fun SignOutDialog(signOut: () -> Unit, closeDialog: () -> Unit = {}) {
    Dialog(onDismissRequest = closeDialog) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, Color(0, 0, 0, 255)),
                    RoundedCornerShape(16.dp)
                )
        ) {
            Column {
                Text(
                    text = "¿Estás seguro de que quieres cerrar sesión?",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = closeDialog,
                        modifier = Modifier
                            .padding(top = 16.dp, end = 5.dp)
                            .width(115.dp)
                    ) {
                        Text("Cancelar", fontSize = 14.sp)
                    }

                    Button(
                        onClick = signOut,
                        modifier = Modifier
                            .padding(top = 16.dp, start = 5.dp)
                            .width(115.dp)
                    ) {
                        Text("Cerrar Sesión", fontSize = 14.sp)
                    }
                }
            }

        }
    }
}