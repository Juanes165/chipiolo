package com.aragang.chipiolo.SignInChipiolo

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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


    var showCreateNameDialog by remember { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    val viewModel = viewModel<SignInViewModel>()
    val focusManager = LocalFocusManager.current

    val showPassword = remember { mutableStateOf(false) }

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
            .background(Color(0, 97, 23, 255))
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
                    .size(325.dp)
            )

            Text(
                text = stringResource(R.string.loiniciar),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(R.string.loemail), fontSize = 16.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                ),
                modifier = Modifier.padding(bottom = 20.dp),
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

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(stringResource(R.string.lopass), fontSize = 16.sp) },
                modifier = Modifier.padding(bottom = 20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White
                ),
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
                        Pair(
                            Icons.Filled.Visibility,
                            Color.White
                        )
                    } else {
                        Pair(Icons.Filled.VisibilityOff, Color.White)
                    }

                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            icon,
                            contentDescription = "Visibility",
                            tint = iconColor
                        )
                    }
                }
            )

            ClickableText(
                text = AnnotatedString(stringResource(R.string.loforgotpass)),
                onClick = { onRecoverPassword() },
                modifier = Modifier
                    .padding(bottom = 20.dp, end = 20.dp)
                    .align(Alignment.End),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )

            Button(onClick = {
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
                    }
                }
            }) {
                Text(text = stringResource(R.string.loiniciar))
            }

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
                }
            ) {
                Text(text = stringResource(R.string.logoogle))
            }

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
                    )
                )
            }
        }
    }
}
