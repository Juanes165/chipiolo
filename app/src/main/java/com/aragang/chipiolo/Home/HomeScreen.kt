package com.aragang.chipiolo.Home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R
import com.aragang.chipiolo.SignInChipiolo.SignInState

@Composable
fun HomeScreen(
    onProfile: () -> Unit = {},
    onSinglePlayer: () -> Unit = {},
    onMultiPlayer: () -> Unit = {}
) {

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont("Bangers")

    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(43, 168, 74))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = { onProfile() },
                colors = buttonColors(containerColor = Color(66, 79, 88)),
                shape = CircleShape,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.End)
            ) {
                Text("P")
            }

            Image(
                painter = painterResource(id = R.drawable.logo_chipiolo),
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(350.dp)
                    .padding(bottom = 70.dp, top = 30.dp, start = 30.dp, end = 30.dp)
            )


            Button(
                onClick = { onSinglePlayer() },
                colors = buttonColors(
                    containerColor = Color(
                        66,
                        79,
                        88
                    )
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp)
            ) {

                //Row {
                Image(
                    painter = painterResource(R.drawable.logo_pica),
                    contentDescription = "Logo de pica",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "UN JUGADOR",
                    Modifier.padding(
                        start = 10.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                )
                //}
            }
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { onMultiPlayer() },
                enabled = false,
                colors = buttonColors(
                    containerColor = Color(
                        66,
                        79,
                        88
                    )
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp)
            )
            {

                Text(
                    text = "MULTIJUGADOR",
                    Modifier.padding(
                        start = 5.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HomeScreen()
}