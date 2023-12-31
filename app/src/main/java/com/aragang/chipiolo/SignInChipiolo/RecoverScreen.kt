package com.aragang.chipiolo.SignInChipiolo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.aragang.chipiolo.API.VerifyCode
import com.aragang.chipiolo.API.recoverPassword
import com.aragang.chipiolo.R
import com.composeuisuite.ohteepee.OhTeePeeInput
import com.composeuisuite.ohteepee.configuration.OhTeePeeCellConfiguration
import com.composeuisuite.ohteepee.configuration.OhTeePeeConfigurations
import kotlinx.coroutines.delay

//@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RecoverScreen(
    onGoBack: () -> Unit = {},
    onCodeSent: () -> Unit = {},
    client: Login
) {

    // Paleta de colores
    val colorDarkGray = colorResource(id = R.color.dark_gray)
    val colorLightGray = colorResource(id = R.color.light_gray)
    val colorWhite = colorResource(id = R.color.white)
    val colorGreenPrimary = colorResource(id = R.color.green_primary)
    val colorBlack = colorResource(id = R.color.black)

    var emailRecover = remember { mutableStateOf("") }
    var showConditionsDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var openExito by remember { mutableStateOf(false) }
    var openError by remember { mutableStateOf(false) }

    // Otp variables
    var otpValue: String by remember { mutableStateOf("") }
    val defaultCellConfig = OhTeePeeCellConfiguration.withDefaults(
        borderColor = Color.LightGray,
        borderWidth = 1.dp,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(
            color = Color.Black
        )
    )

    // Contador de intentos:
    var intentos = remember { mutableStateOf(0) }
    var enableSend = remember { mutableStateOf(true) }
    var contador = remember { mutableStateOf(0) }
    var contarTiempo by remember {mutableStateOf(false) }

    var abbleToVerify = remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

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
                text = stringResource(R.string.recov_pass),
                color = colorWhite,
                fontSize = 35.sp,
                modifier = Modifier
                        .padding(bottom = 0.dp)
                        .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.recov_pass_two),
                color = colorWhite,
                fontSize = 35.sp,
                modifier = Modifier
                        .padding(bottom = 15.dp, top = 0.dp)
                        .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )

            // Subtitulo
            Text(
                text = stringResource(R.string.recov_steps),
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            // Campo de email
            OutlinedTextField(
                value = emailRecover.value,
                onValueChange = { emailRecover.value = it },
                label = { Text(text = stringResource(R.string.recemail), fontSize = 16.sp) },
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

            // Boton de enviar
            Button(
                enabled = enableSend.value,
                onClick = {
                    showConditionsDialog = true
                    recoverPassword(emailRecover.value)
                    //onCodeSent() Por ahora necesito que se abra la alerta
                },
                modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorGreenPrimary,
                    contentColor = colorWhite,
                    disabledContainerColor = colorWhite,
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(bottom = 15.dp, top = 15.dp)
            ) {
                Text(
                    text = stringResource(R.string.send_recover),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

    }

    if (showConditionsDialog) {
        AlertDialog(
            onDismissRequest = { showConditionsDialog = false },
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 410.dp),
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
            )
        ) {

            Column(
                modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                        .background(Color.White)
                        .padding(end = 18.dp, start = 18.dp, top = 38.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val keyboardController =
                    LocalSoftwareKeyboardController.current //Necesito el ExperimentalcomposeUiApi
                val focusManager = LocalFocusManager.current

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Column {
                        androidx.compose.material.Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Ingrese el código que le enviamos a su correo",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(40.dp))

                        OhTeePeeInput(
                            value = otpValue,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                            onValueChange = { newValue, isValid ->
                                otpValue = newValue
                                abbleToVerify.value = isValid
                            },
                            isValueInvalid = openError,
                            configurations = OhTeePeeConfigurations.withDefaults(
                                cellsCount = 6,
                                emptyCellConfig = defaultCellConfig,
                                cellModifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(48.dp),
                                filledCellConfig = defaultCellConfig,
                                activeCellConfig = defaultCellConfig.copy(
                                    borderColor = Color.Blue,
                                    borderWidth = 2.dp
                                ),
                                errorCellConfig = defaultCellConfig.copy(
                                    borderColor = Color.Red,
                                    borderWidth = 2.dp
                                ),
                                placeHolder = "-",
                            ),
                        )


                        //Columnscope para los botones
                        Row(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(bottom = 19.dp)
                                    .weight(1f),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {

                            androidx.compose.material.Button(
                                onClick = {  showConditionsDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color(0xFF9300FC),
                                    disabledBackgroundColor = Color(0x009300FC)
                                ),
                                enabled = true,
                                modifier = Modifier
                                        .weight(1f)
                                        .border(
                                                2.5.dp,
                                                Color(0xFFF5B041),
                                                shape = RoundedCornerShape(100.dp)
                                        ),
                                shape = RoundedCornerShape(100.dp),
                                elevation = ButtonDefaults.elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp
                                )
                            ) {
                                androidx.compose.material.Text(
                                    "Cancelar",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(end = 6.dp, top = 6.dp, bottom = 6.dp, start = 6.dp),
                                    color = Color(0xFFE67E22)
                                )

                            }

                            Spacer(Modifier.width(6.dp))

                            androidx.compose.material.Button(
                                onClick = {
                                    VerifyCode(
                                        emailRecover.value,
                                        otpValue,
                                        coroutineScope,
                                        client,
                                        { openExito = true },
                                        { openError = true })
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFFF5B041),
                                    contentColor = Color.White,
                                    disabledBackgroundColor = Color(0x7A9300FC)
                                ),
                                enabled = abbleToVerify.value,
                                shape = RoundedCornerShape(100.dp),
                                modifier = Modifier.weight(1f),

                                ) {

                                androidx.compose.material.Text(
                                    "Verificar",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 6.dp, top = 6.dp, bottom = 6.dp)
                                )
                            }
                        }

                        if (openExito) {
                            AlertDialog(
                                onDismissRequest = {
                                    openExito = false
                                },
                                title = {
                                    Text(text = "Codigo Exitoso")
                                },
                                text = {
                                    Text("Se ha enviado un correo a ${emailRecover.value} para recuperar su contraseña")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            openExito = false
                                            showConditionsDialog = false
                                            otpValue = ""
                                            onGoBack()
                                        }) {
                                        Text("Ok")
                                    }
                                }
                            )
                        }

                        if (openError) {
                            AlertDialog(
                                onDismissRequest = {
                                    openError = false
                                },
                                title = {
                                    Text(text = "Código Incorrecto")
                                },
                                text = {
                                    if (intentos.value != 3)
                                        Text("Intente nuevamente, le quedan ${3 - intentos.value} intentos")
                                    else
                                        Text("Has sido bloqueado por 20 segundos")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            intentos.value += 1
                                            openError = false
                                            otpValue = ""

                                        }) {
                                        Text("Ok")
                                    }
                                }
                            )
                        }

                        if(intentos.value == 4){
                            enableSend.value = false
                            showConditionsDialog = false
                            contarTiempo = true
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (contarTiempo) {
        val blockTimeInSeconds = 20
        LaunchedEffect(contarTiempo) {
            while (contador.value < blockTimeInSeconds) {
                delay(1000)
                contador.value += 1
            }
            enableSend.value = true
            contador.value = 0
            intentos.value = 0
            contarTiempo = false
        }
    }
}



