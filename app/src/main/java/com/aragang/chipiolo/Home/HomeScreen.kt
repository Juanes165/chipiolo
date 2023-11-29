package com.aragang.chipiolo.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R

@Composable
fun HomeScreen(
    onProfile: () -> Unit = {},
    onSinglePlayer: () -> Unit = {},
    onMultiPlayer: () -> Unit = {}
) {

    // Paleta de colores
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)

    val provider = GoogleFont.Provider(
        providerAuthority = stringResource(R.string.letra_providerAuthority),
        providerPackage = stringResource(R.string.letra_providerPackage),
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont("Bangers")

    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDarkGray)
            .padding(start = 60.dp, end = 60.dp, top = 10.dp, bottom = 10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_chipiolo),
                contentDescription = stringResource(R.string.logo_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(350.dp)
            )

            // Boton single player
            Button(
                onClick = { onSinglePlayer() },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                colors = buttonColors(
                    containerColor = colorGreenPrimary,
                    contentColor = colorWhite
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(bottom = 15.dp, top = 15.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.logo_pica),
                    contentDescription = stringResource(R.string.pica_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                )

                Text(
                    text = stringResource(R.string.single_player),
                    Modifier.padding(
                        start = 15.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                )
            }

            // Boton multi player
            Button(
                onClick = { onSinglePlayer() },
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 20.dp)
                    .fillMaxWidth(),
                colors = buttonColors(
                    containerColor = colorGreenPrimary,
                    contentColor = colorWhite
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(bottom = 15.dp, top = 15.dp),
                enabled = false
            ) {

                Image(
                    painter = painterResource(R.drawable.logo_pica),
                    contentDescription = stringResource(R.string.pica_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                )

                Text(
                    text = stringResource(R.string.multi_player),
                    Modifier.padding(
                        start = 15.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                )
            }

            // Boton de perfil
            Button(
                onClick = { onProfile() },
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 20.dp)
                    .fillMaxWidth(),
                colors = buttonColors(
                    containerColor = colorGreenPrimary,
                    contentColor = colorWhite
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(bottom = 15.dp, top = 15.dp),
            ) {

                Image(
                    painter = painterResource(R.drawable.logo_pica),
                    contentDescription = stringResource(R.string.pica_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                )

                Text(
                    text = stringResource(R.string.profile),
                    Modifier.padding(
                        start = 15.dp
                    ),
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
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