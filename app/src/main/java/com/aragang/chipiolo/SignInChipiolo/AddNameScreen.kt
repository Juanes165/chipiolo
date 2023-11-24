package com.aragang.chipiolo.SignInChipiolo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R
import com.google.android.gms.auth.api.identity.SignInClient

@Composable
fun AddNameScreen(
    client: Login,
    onNameEntered: (String) -> Unit,
    onSuccess: () -> Unit,
) {


    val name = remember { mutableStateOf("") }

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
            )

            Text(
                text = "Bienvenido a Chipiolo",
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(
                text = "Crea un nombre de usuario genial para empezar a jugar",
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Nombre", fontSize = 16.sp) },
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
            Button(onClick = {
                onNameEntered(name.value)
            }) {
                Text(text = "Continuar")
            }
        }
    }
}
