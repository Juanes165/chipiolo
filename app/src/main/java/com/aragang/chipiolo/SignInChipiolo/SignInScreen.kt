package com.aragang.chipiolo.SignInChipiolo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont("Bangers")

    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(43, 168, 74))) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Image(
                painter = painterResource(id = R.drawable.logo_chipiolo),
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(350.dp).padding(bottom = 70.dp))


            androidx.compose.material3.Button(
                onClick = { onSignInClick() },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(66, 79, 88)),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp)) {

                //Row {
                Image(
                    painter = painterResource(R.drawable.logo_pica),
                    contentDescription = "Logo de pica",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp))

                androidx.compose.material3.Text(
                    text = "UN JUGADOR",
                    Modifier.padding(
                        start = 10.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,)
                //}
            }
            Spacer(modifier = Modifier.height(30.dp))

            androidx.compose.material3.Button(
                onClick = { /*TODO*/ },
                enabled = false,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(66, 79, 88)),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp)) {

                androidx.compose.material3.Text(
                    text = "MULTIJUGADOR",
                    Modifier.padding(
                        start = 5.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp)
            }
        }
    }

    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(300.dp)
                .width(300.dp)
                .clip(shape = MaterialTheme.shapes.medium),

        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.araganes),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onSignInClick()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.white)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { *//*TODO*//* },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Text(
                text = stringResource(id = R.string.guest),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.purple_200)
            )
        }
    }*/
}