package com.aragang.chipiolo.CreateUserScreen

import android.util.Log
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.aragang.chipiolo.R
import com.aragang.chipiolo.SignInChipiolo.Login
import com.aragang.chipiolo.SignInChipiolo.SignInState
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.CoroutineScope


@Composable
fun CreateUserScreen(
    client: Login,
    onRegisterSuccess: () -> Unit = {},
    onLogin: () -> Unit = {},
    goToHome: () -> Unit = {}
) {

    // Paleta de colores
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)

    var visibilityIconColor by remember { mutableStateOf(colorLightGray) }

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var repeatPassword = remember { mutableStateOf("") }

    var showConditionsDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val showPassword = remember { mutableStateOf(false) }
    val showRepeatPassword = remember { mutableStateOf(false) }


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
                contentDescription = stringResource(R.string.culogo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(225.dp)
            )

            // Titulo
            Text(
                text = stringResource(R.string.cuchipiuser),
                color = colorWhite,
                fontSize = 35.sp,
                modifier = Modifier.padding(bottom = 15.dp),
                fontWeight = FontWeight.Bold
            )

            // Email
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(R.string.cucorreo), fontSize = 16.sp) },
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

            // Password
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(stringResource(R.string.cupass), fontSize = 16.sp) },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
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

            //  RepassWord
            OutlinedTextField(
                value = repeatPassword.value,
                onValueChange = { repeatPassword.value = it },
                label = { Text(stringResource(R.string.curptpass), fontSize = 16.sp) },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
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
                visualTransformation = if (showRepeatPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val (icon, iconColor) = if (showRepeatPassword.value) {
                        Pair(Icons.Filled.Visibility, visibilityIconColor)
                    } else {
                        Pair(Icons.Filled.VisibilityOff, visibilityIconColor)
                    }

                    IconButton(onClick = { showRepeatPassword.value = !showRepeatPassword.value }) {
                        Icon(
                            icon,
                            contentDescription = "Visibility",
                            tint = iconColor,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }
            )

            // Texto de condiciones de password
            Text(
                text = stringResource(returnPasswordMessage(password.value, repeatPassword.value)),
                color = colorWhite,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp, start = 24.dp, end = 24.dp)
            )

            // Boton de registro
            Button(
                onClick = {
                    if (returnPasswordMessage(
                            password.value,
                            repeatPassword.value
                        ) == R.string.empty
                        && email.value != ""
                    ) {
                        showConditionsDialog = true
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
                    text = stringResource(R.string.register),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            // Texto de cuenta existente
            Row(
                modifier = Modifier
                    .padding(top = 20.dp),
            ) {
                Text(
                    text = stringResource(R.string.cuyacuenta),
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.cuinicia)),
                    onClick = { onLogin() },
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            if (showConditionsDialog) {
                PopupWindowDialog(
                    client = client,
                    email = email.value,
                    password = password.value,
                    coroutineScope = rememberCoroutineScope(),
                    closeDialog = { showConditionsDialog = false },
                    onRegisterSuccess = onRegisterSuccess
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupWindowDialog(
    client: Login,
    email: String,
    password: String,
    coroutineScope: CoroutineScope,
    closeDialog: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
) {
    // on below line we are creating variable for button title
    // and open dialog.
    val openDialog = remember { mutableStateOf(false) }
    val register = stringResource(R.string.register)
    val buttonTitle = remember {
        mutableStateOf(register)
    }

    AlertDialog(
        // on below line we are adding
        // alignment and properties.
        onDismissRequest = closeDialog,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp),
    ) {

        // on the below line we are creating a box.
        Box(
            // adding modifier to it.
            Modifier
                // on below line we are adding background color
                .background(Color.White, RoundedCornerShape(20.dp))
                // on below line we are adding border.
                .border(1.dp, color = Color.Black, RoundedCornerShape(20.dp))
                .padding(top = 15.dp, bottom = 15.dp)
                .fillMaxWidth()
        ) {

            // on below line we are adding column

            Column(
                // on below line we are adding modifier to it.
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                // on below line we are adding horizontal and vertical
                // arrangement to it.
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    // on below line we are adding text to
                    // our dialog.
                    text = stringResource(R.string.conditions),
                    // on below line we are adding color
                    // to our text.
                    color = Color.Black,
                    // on below line we are adding padding
                    // to our text.
                    modifier = Modifier.padding(vertical = 5.dp),
                    // on below line we are adding font size
                    // to our text.
                    fontSize = 20.sp
                )
                // on below line we are adding text for our pop up
                Text(
                    // on below line we are specifying text
                    text = stringResource(R.string.conditions_text),
                    // on below line we are specifying color.
                    color = Color.Black,
                    // on below line we are adding padding to it
                    modifier = Modifier
                        .padding(vertical = 5.dp),
                    // on below line we are adding font size.
                    fontSize = 14.sp
                )
                Button(

                    // on below line we are adding modifier.
                    // and padding to it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),

                    // on below line we are adding
                    // on click to our button
                    onClick = {

                        // on below line we are updating
                        // boolean value of open dialog.
                        openDialog.value = !openDialog.value
                        if (!openDialog.value) {

                            // on below line we are updating value
                            buttonTitle.value = register
                        }
                        coroutineScope.launch {
                            val signInResult =
                                client.createUserWithEmailAndPassword(email, password)
                            if (signInResult.data != null) {
                                val user = signInResult.data
                                if (user.name.isNullOrEmpty()) {
                                    onRegisterSuccess()
                                } else {
                                    closeDialog()
                                    Log.e("Error", "Error al crear usuario $signInResult")
                                }
                            }
                        }
                    }
                ) {

                    // on the below line we are creating a text for our button.
                    Text(text = stringResource(R.string.accept), modifier = Modifier.padding(3.dp))
                }
            }
        }
    }
}

private fun strengthChecker(password: String): StrengthPasswordTypes =
    when {
        REGEX_STRONG_PASSWORD.toRegex().containsMatchIn(password) -> StrengthPasswordTypes.STRONG
        else -> StrengthPasswordTypes.WEAK
    }

enum class StrengthPasswordTypes {
    STRONG,
    WEAK
}

private const val REGEX_STRONG_PASSWORD =
    "(?=.*[a-z])(?=.*[0-9])(?=.{8,})"

fun returnPasswordMessage(password: String, repeatPassword: String): Int {
    return if (password == "") {
        R.string.empty
    } else if (strengthChecker(password) == StrengthPasswordTypes.WEAK) {
        R.string.password_conditions
    } else if (password != repeatPassword) {
        R.string.passwords_different
    } else {
        R.string.empty
    }
}