package com.aragang.chipiolo.Profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import com.aragang.chipiolo.R
import com.aragang.chipiolo.SignInChipiolo.Login
import com.aragang.chipiolo.SignInChipiolo.UserData
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay


@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    onCamera: () -> Unit,
    onAvatarPick: () -> Unit,
    goToLogin: () -> Unit = {},
    reload: () -> Unit = {},
    goToProfileHome: () -> Unit = {},
    client: Login,
) {

    // Paleta de colores
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)
    val colorRed = colorResource(id = R.color.red_primary)

    var showSignOutDialog by remember { mutableStateOf(false) }
    var showProfilePictureDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showConfirmPictureDialog by remember { mutableStateOf(false) }

    var showChangeName by remember { mutableStateOf(false) }
    var showChangeEmail by remember { mutableStateOf(false) }

    var photoUri: Uri? by remember { mutableStateOf(null) }

    // Photo picker
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //When the user has selected a photo, its URL is returned here
            photoUri = uri
            if (photoUri != null) {
                showConfirmPictureDialog = true
            }
        }

    BackHandler {
        goToProfileHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 100.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 80.dp, top = 20.dp)

            ) {
                Image(
                    painter = painterResource(R.drawable.logo_pica),
                    contentDescription = "pica",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable { goToLogin() }
                )
                Text(
                    text = stringResource(R.string.user_profile),
                    fontSize = 40.sp,
                    modifier = Modifier.padding(start = 20.dp),
                    fontWeight = FontWeight(700),
                )
            }

            // FOTO DE PERFIL
            if (userData?.profileImage != null) {
                ProfilePicture(
                    profilePictureUrl = userData.profileImage,
                    size = 200.dp,
                    clickableFun = {
                        showProfilePictureDialog = true
                    })
            } else {
                Image(
                    painter = painterResource(R.drawable.avatar0),
                    contentDescription = "Default profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .border(
                            BorderStroke(4.dp, Color.White),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable { showProfilePictureDialog = true }
                )
            }

            if (userData != null) {

                Text(
                    text = stringResource(R.string.name),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, top = 16.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                // NOMBRE Y ACTUALIZAR NOMBRE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        // get first name
                        text = userData.name?.substringBefore(' ') ?: "ChipiUsuario",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        fontWeight = FontWeight.SemiBold,
                    )
                    IconButton(onClick = { showChangeName = true }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "edit",
                            tint = colorBlack,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(40.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.loemail),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, top = 16.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                // EMAIL Y ACTUALIZAR EMAIL
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = userData.email,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .horizontalScroll(rememberScrollState()),

                        )
                }
            }
        }

        // BOTON DE CERRAR SESION
        Button(
            onClick = { showSignOutDialog = true },
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorRed,
                contentColor = colorWhite,
            ),
            shape = MaterialTheme.shapes.medium,

            ) {
            Text(
                text = stringResource(R.string.clses),
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                color = colorWhite
            )
        }

        IconButton(onClick = { showDeleteAccountDialog = true },
            modifier = Modifier
                .padding(bottom = 24.dp, end = 24.dp)
                .align(Alignment.BottomEnd)
                .background(color = colorRed, shape = CircleShape)
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "delete",
                tint = colorWhite,
                modifier = Modifier
//                    .padding(start = 10.dp)
                    .size(40.dp)
            )
        }

        // DIALOGO PARA CAMBIAR NOMBRE
        if (showChangeName) {
            editValueDialog(
                closeDialog = { showChangeName = false },
                initialValue = userData?.name ?: "",
                valueType = "name",
                updateValue = { newName ->
                    client.updateName(newName)
                    showChangeName = false
                    reload()
                }
            )
        }

        // DIALOGO PARA CAMBIAR EMAIL
        if (showChangeEmail) {
            editValueDialog(
                closeDialog = { showChangeEmail = false },
                initialValue = userData?.email ?: "",
                valueType = "email",
                updateValue = { newEmail ->
                    client.updateEmail(newEmail)
                    showChangeEmail = false
                }
            )
        }

        // DIALOGO DE CERRAR SESION
        if (showSignOutDialog) {
            SignOutDialog(
                signOut = onSignOut,
                closeDialog = { showSignOutDialog = false }
            )
        }

        // DIALOGO DE CAMBIAR FOTO DE PERFIL
        if (showProfilePictureDialog) {
            ProfilePictureDialog(
                closeDialog = { showProfilePictureDialog = false },
                openCamera = onCamera,
                openGallery = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                    Log.d("TAG", "PHOTOURI BEFORE: ${photoUri}")
                },
                openAvatarPick = {
                    onAvatarPick()
                    showProfilePictureDialog = false
                },
            )
        }

        // DIALOGO DE BORRAR CUENTA
        if (showDeleteAccountDialog) {
            deleteAccountDialog(
                closeDialog = { showDeleteAccountDialog = false },
                deleteAccount = {
                    client.deleteAccount()
                    goToLogin()
                }
            )
        }

        // DIALOGO CON LA PREVIEW DE LA FOTO
        if (showConfirmPictureDialog) {
            Dialog(onDismissRequest = { showConfirmPictureDialog = false }) {
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
                            text = "Tu foto",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )

                        AsyncImage(
                            model = photoUri,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(4.dp, Color.White),
                                    CircleShape
                                )
                                .padding(4.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { showProfilePictureDialog = false },
                                modifier = Modifier
                                    .padding(top = 16.dp, end = 5.dp)
                                    .width(115.dp),
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Text("Repetir", fontSize = 14.sp)
                            }

                            Button(
                                onClick = {
                                    uploadProfilePicture(
                                        Uri.parse(photoUri.toString()),
                                        client,
                                        // crop email removing the domain
                                        userData?.email?.substringBefore('@') ?: userData?.id ?: "",
                                        updatePhotoUri = { newPhotoUri ->
                                            photoUri = Uri.parse(newPhotoUri)
                                        }
                                    )
                                    showConfirmPictureDialog = false
                                    showProfilePictureDialog = false
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp, start = 5.dp)
                                    .width(115.dp),
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Text("Aceptar", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}


fun uploadProfilePicture(
    uri: Uri,
    client: Login,
    userMail: String,
    updatePhotoUri: (String) -> Unit = {}
) {
    Log.d("TAG", "PHOTOURI: ${uri.lastPathSegment}")
    val storageRef = FirebaseStorage.getInstance().getReference()
    val fileRef = storageRef.child("${if (userMail == "") uri.lastPathSegment else userMail}")
    val uploadTask = fileRef.putFile(uri)

    val urlTask = uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        fileRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            Log.d("TAG", "uploadProfilePicture: $downloadUri")
            client.updatePhotoUrl(downloadUri.toString())
            updatePhotoUri(downloadUri.toString())
        } else {
            Log.e("Error", "uploadProfilePicture: ${task.exception?.message}")
        }
    }
}


@Composable
fun ProfilePictureDialog(
    openCamera: () -> Unit = {},
    openGallery: () -> Unit = {},
    openAvatarPick: () -> Unit = {},
    closeDialog: () -> Unit = {},
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
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.picpro),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = openCamera,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(115.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(stringResource(R.string.cam), fontSize = 18.sp)
                }

                Button(
                    onClick = openGallery,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(115.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(stringResource(R.string.gal), fontSize = 18.sp)
                }

                Button(
                    onClick = openAvatarPick,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .width(115.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(stringResource(R.string.ava), fontSize = 18.sp)
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
                    text = stringResource(R.string.confexit),
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
                            .width(115.dp),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(stringResource(R.string.cnl), fontSize = 14.sp)
                    }

                    Button(
                        onClick = signOut,
                        modifier = Modifier
                            .padding(top = 16.dp, start = 5.dp)
                            .width(115.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(141, 0, 0, 255),
                            contentColor = Color.White,
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(stringResource(R.string.salir), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun deleteAccountDialog(
    closeDialog: () -> Unit,
    deleteAccount: () -> Unit = {}
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
                    text = stringResource(R.string.elicuen),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    color = Color(255, 0, 0)
                )
                Text(
                    text = stringResource(R.string.msgfarewell),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp),
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
                            .width(115.dp),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(stringResource(R.string.cnl), fontSize = 14.sp)
                    }

                    Button(
                        onClick = deleteAccount,
                        modifier = Modifier
                            .padding(top = 16.dp, start = 5.dp)
                            .width(115.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(141, 0, 0, 255),
                            contentColor = Color.White,
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(stringResource(R.string.elim), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun editValueDialog(
    closeDialog: () -> Unit,
    initialValue: String,
    valueType: String,
    updateValue: (String) -> Unit = {},
) {
    var fieldValue = remember { mutableStateOf(initialValue) }
    val title = when (valueType) {
        "name" -> "Cambiar nombre"
        "email" -> "Cambiar email"
        else -> "Cambiar valor"
    }

    val label = when (valueType) {
        "name" -> "Nombre"
        "email" -> "Email"
        else -> "Valor"
    }

    Dialog(
        onDismissRequest = closeDialog
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, Color(0, 0, 0, 255)),
                    RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 20.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
            ) {
                Text(
                    text = title,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                OutlinedTextField(
                    value = fieldValue.value,
                    onValueChange = { fieldValue.value = it },
                    label = { Text(label) }
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(
                        onClick = closeDialog,
                        modifier = Modifier
                            .padding(top = 16.dp, end = 5.dp)
                            .width(115.dp),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text("Cancelar", fontSize = 14.sp)
                    }

                    Button(
                        onClick = { updateValue(fieldValue.value) },
                        modifier = Modifier
                            .padding(top = 16.dp, start = 5.dp)
                            .width(115.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(125, 189, 76, 255),
                            contentColor = Color.White,
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text("Actualizar", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}