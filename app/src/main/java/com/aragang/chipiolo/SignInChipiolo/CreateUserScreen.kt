package com.aragang.chipiolo.CreateUserScreen

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.res.stringResource
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
//    viewModel: SignInViewModel,
//    oneTapClient: SignInClient,
//    onLoginSuccess: (UserData) -> Unit,
//    onLoginFailure: (String) -> Unit
    client: Login,
    onRegisterSuccess: () -> Unit = {},
    onLogin: () -> Unit = {},
    goToHome: () -> Unit = {}
) {


    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    var showConditionsDialog by remember { mutableStateOf(false) }


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
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(325.dp)
            )

            Text(
                text = "Crea Tu ChipiUser",
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 50.dp)
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Correo", fontSize = 16.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                ),
                modifier = Modifier.padding(bottom = 20.dp),
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña", fontSize = 16.sp) },
                modifier = Modifier.padding(bottom = 20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White
                ),
            )

            //  RepassWord
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Repite la contraseña", fontSize = 16.sp) },
                modifier = Modifier.padding(bottom = 20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White
                ),
            )

            // Button
            Button(
                onClick = {
                    showConditionsDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(66, 79, 88)
                ),
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(
                    text = stringResource(R.string.register),
                    fontSize = 20.sp,
                )
            }


            Row(
                modifier = Modifier
                    .padding(top = 20.dp),
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                ClickableText(
                    text = AnnotatedString("Inicia Sesión"),
                    onClick = { onLogin() },
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                )
            }

            if (showConditionsDialog){
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
                            val signInResult = client.createUserWithEmailAndPassword(email, password)
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
                ){

                    // on the below line we are creating a text for our button.
                    Text(text = stringResource(R.string.accept), modifier = Modifier.padding(3.dp))
                }
            }
        }
    }
    }


