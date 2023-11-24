package com.aragang.chipiolo.CreateUserScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
) {

    val auth = FirebaseAuth.getInstance()




    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }




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

            androidx.compose.material3.Text(
                text = "Crea Tu ChipiUser",
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 50.dp)
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { androidx.compose.material3.Text(text = "Correo", fontSize = 16.sp) },
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
                label = { androidx.compose.material3.Text("Contraseña", fontSize = 16.sp) },
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

//            RepassWord
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { androidx.compose.material3.Text("Repite la contraseña", fontSize = 16.sp) },
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
            PopupWindowDialog(client, email.value, password.value, coroutineScope)

        }
    }
}

@Composable
fun PopupWindowDialog(
    client: Login,
    email: String,
    password: String,
    coroutineScope: CoroutineScope
) {
    // on below line we are creating variable for button title
    // and open dialog.
    val openDialog = remember { mutableStateOf(false) }
    val register = stringResource(R.string.register)
    val buttonTitle = remember {
        mutableStateOf(register)
    }

    // on the below line we are creating a column
    Column(

        // in this column we are specifying
        // modifier to add padding and fill
        // max size
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        // on below line we are adding horizontal alignment
        // and vertical arrangement
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // on the below line we are creating a button
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

                // on below line we are checking if dialog is close
                if (!openDialog.value) {

                    // on below line we are updating value
                    buttonTitle.value = register
                }
            }
        ) {

            // on the below line we are creating a text for our button.
            Text(text = buttonTitle.value, modifier = Modifier.padding(3.dp))
        }

        // on below line we are creating a box to display box.
        Box {
            // on below line we are specifying height and width
            val popupWidth = 900.dp
            val popupHeight = 200.dp

            // on below line we are checking if dialog is open
            if (openDialog.value) {
                // on below line we are adding pop up
                Popup(
                    // on below line we are adding
                    // alignment and properties.
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties()
                ) {

                    // on the below line we are creating a box.
                    Box(
                        // adding modifier to it.
                        Modifier
                            .size(popupWidth, popupHeight)
                            .padding(top = 5.dp)
                            // on below line we are adding background color
                            .background(colorResource(R.color.black), RoundedCornerShape(10.dp))
                            // on below line we are adding border.
                            .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
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
                            // on below line we are adding text for our pop up
                            Text(
                                // on below line we are specifying text
                                text = stringResource(R.string.conditions_text),
                                // on below line we are specifying color.
                                color = Color.White,
                                // on below line we are adding padding to it
                                modifier = Modifier
                                    .padding(vertical = 5.dp),
                                // on below line we are adding font size.
                                fontSize = 16.sp
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
                                        client.createUserWithEmailAndPassword(email, password)
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
        }
    }
}
