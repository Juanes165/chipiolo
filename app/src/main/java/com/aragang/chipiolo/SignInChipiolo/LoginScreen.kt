package com.aragang.chipiolo.SignInChipiolo

import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aragang.chipiolo.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    client: Login,
    onSuccess: () -> Unit,
    onRegister: () -> Unit,
    onRecoverPassword: () -> Unit
) {

    // Paleta de colores
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)

    var visibilityIconColor by remember { mutableStateOf(colorLightGray) }

    var showCreateNameDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    val viewModel = viewModel<SignInViewModel>()
    val focusManager = LocalFocusManager.current

    val showPassword = remember { mutableStateOf(false) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = client.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
                onSuccess()
            } else {
                Log.d("Login", "onActivityResult: ${result.resultCode}")
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
            .padding(start = 60.dp, end = 60.dp, top = 10.dp, bottom = 10.dp)
    ) {
        // formulario de email y password
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_chipiolo),
                contentDescription = stringResource(R.string.lologo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(225.dp)
//                    .shadow(
//                        5.dp,
//                        shape = MaterialTheme.shapes.medium,
//                        ambientColor = colorWhite,
//                        spotColor = colorWhite
//                    )
            )

            // Titulo
            Text(
                text = stringResource(R.string.loiniciar),
                color = colorWhite,
                fontSize = 35.sp,
                modifier = Modifier.padding(bottom = 15.dp),
                fontWeight = FontWeight.Bold
            )

            // Campo de email
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(R.string.loemail), fontSize = 16.sp) },
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
                singleLine = true,
            )

            // Campo de password
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(stringResource(R.string.lopass), fontSize = 16.sp) },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        visibilityIconColor = if (it.isFocused) colorWhite else colorLightGray
                    },
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
                singleLine = true,
                visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val (icon, iconColor) = if (showPassword.value) {
                        Pair(Icons.Filled.Visibility, visibilityIconColor)
                    } else {
                        Pair(Icons.Filled.VisibilityOff, visibilityIconColor)
                    }
                    // Icono de visibilidad de password
                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            icon,
                            contentDescription = "Visibility",
                            tint = iconColor,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }
            )

            // Texto olvidaste tu contraseña
            ClickableText(
                text = AnnotatedString(stringResource(R.string.loforgotpass)),
                onClick = { onRecoverPassword() },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .align(Alignment.End),
                style = TextStyle(
                    color = colorWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )

            // Boton iniciar sesion
            Button(
                onClick = {
                    coroutineScope.launch {
                        val signInResult =
                            client.signInWithEmailAndPassword(email.value, password.value)
                        if (signInResult.data != null) {
                            val user = signInResult.data
                            if (user.name.isNullOrEmpty()) {
                                showCreateNameDialog = true
                            } else {
                                onSuccess()
                            }
                        } else {
                            sendToastMsg("Credenciales Incorrectas", context)
                        }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorGreenPrimary,
                    contentColor = colorWhite
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(bottom = 15.dp, top = 15.dp)
            ) {
                Text(
                    text = stringResource(R.string.loiniciar),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            // Linea divisoria
            Row (
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(100.dp)
                        .background(colorWhite)
                )
                Text(
                    text = stringResource(R.string.or),
                    color = colorWhite,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(100.dp)
                        .background(colorWhite)
                )
            }

            // Boton iniciar sesion con google
            Button(
                onClick = {
                    coroutineScope.launch {
                        val signInIntentSender = client.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorWhite,
                    contentColor = colorBlack
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(bottom = 0.dp, top = 0.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_google),
                        contentDescription = "google",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 15.dp)
                    )
                    Text(
                        text = stringResource(R.string.logoogle),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 15.dp, top = 15.dp, end = 10.dp)
                    )
                }
            }

//            // Boton iniciar sesion con google
//            Button(
//                onClick = {
//                    coroutineScope.launch {
//                        val signInIntentSender = client.signIn()
//                        launcher.launch(
//                            IntentSenderRequest.Builder(
//                                signInIntentSender ?: return@launch
//                            ).build()
//                        )
//                    }
//                },
//                modifier = Modifier
//                    .padding(bottom = 20.dp)
//                    .fillMaxWidth()
//                    .border(
//                        width = 1.dp,
//                        color = colorWhite,
//                        shape = MaterialTheme.shapes.medium
//                    ),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = colorDarkGray,
//                    contentColor = colorWhite
//                ),
//                shape = MaterialTheme.shapes.medium,
//                contentPadding = PaddingValues(bottom = 0.dp, top = 0.dp)
//            ) {
//                Row (
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.logo_google),
//                        contentDescription = "google",
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .size(40.dp)
//                            .padding(end = 15.dp)
//                    )
//                    Text(
//                        text = stringResource(R.string.logoogle),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        modifier = Modifier.padding(bottom = 15.dp, top = 15.dp, end = 10.dp)
//                    )
//                }
//            }

            // Texto de no tienes cuenta
            Row(
                modifier = Modifier
                    .padding(top = 20.dp),
            ) {
                Text(
                    text = stringResource(R.string.lonocuenta),
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.loregistrate)),
                    onClick = { onRegister() },
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}

fun sendToastMsg(msg: String, context: Context){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}