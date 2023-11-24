package com.aragang.chipiolo.SignInChipiolo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.API.BodyRequestModel
import com.aragang.chipiolo.API.BodyRequestModelVerify
//import com.aragang.chipiolo.API.BottomActionButtons
import com.aragang.chipiolo.API.FireStoreAPI
//import com.aragang.chipiolo.API.OtpTextField
import com.aragang.chipiolo.API.ResponseGenerateCode
import com.aragang.chipiolo.API.ResponseVerifyCode
import com.aragang.chipiolo.BuildConfig
import com.aragang.chipiolo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//@Preview
@Composable
fun RecoverScreen(
    onGoBack: () -> Unit = {},
    onCodeSent: () -> Unit = {},
    client: Login
) {
    var emailRecover = remember { mutableStateOf("") }
    var showConditionsDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
                contentDescription = stringResource(R.string.reclogo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(325.dp)
            )

            Text(
                text = stringResource(R.string.recov_pass),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = stringResource(R.string.recov_steps),
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            OutlinedTextField(
                value = emailRecover.value,
                onValueChange = { emailRecover.value = it },
                label = { Text(text = stringResource(R.string.recemail), fontSize = 16.sp) },
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

            Row {
                Button(onClick = { onGoBack() }) {
                    Text(text = stringResource(R.string.reccancel))
                }
                Button(
                    onClick = {
                        showConditionsDialog = true
                        recoverPassword(emailRecover.value)
                        //onCodeSent() Por ahora necesito que se abra la alerta

                    }
                ) {
                    Text(text = stringResource(R.string.send_recover))
                }
            }
        }

    }
    if (showConditionsDialog) {
        PopVerifyCode(closeDialog = { showConditionsDialog = false }, emailRecover, coroutineScope, client)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PopVerifyCode(
    closeDialog: () -> Unit = {},
    email: MutableState<String>,
    coroutineScope: CoroutineScope,
    client: Login
) {
    val openDialog = remember { mutableStateOf(false) }
    val codeTxtFieldTxt = remember { mutableStateOf("") }
    val textFieldRequester = remember { FocusRequester() }
    val codigo = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {closeDialog},
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 420.dp)) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                .background(Color.White)
                .padding(end = 18.dp, start = 18.dp, top = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val keyboardController = LocalSoftwareKeyboardController.current //Necesito el ExperimentalcomposeUiApi
            val focusManager = LocalFocusManager.current

            Box (modifier=Modifier.weight(1f), contentAlignment = Alignment.Center){

                /*TextField(
                    value = codeTxtFieldTxt.value,
                    onValueChange = {
                        if (it.length <= 6) {
                            codeTxtFieldTxt.value = it
                            if(it.length == 6){
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        } else {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                        Log.d("CODE", "Now: ${codeTxtFieldTxt.value}")
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .size(0.dp)
                        .focusRequester(textFieldRequester)
                        .alpha(0f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )*/


                Column{
                    androidx.compose.material.Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Ingrese el código que le enviamos a su correo",
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(18.dp))


                    OutlinedTextField(
                        value = codigo.value,
                        onValueChange = { codigo.value = it },
                        label = { Text(text = stringResource(R.string.recemail), fontSize = 16.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedTextColor = Color.Black,
                        ),
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    /*OtpTextField(codeText = codeTxtFieldTxt) {
                        focusManager.clearFocus()
                        textFieldRequester.requestFocus()
                    }*/


                    /*Spacer(Modifier.height(12.dp))

                    TextField(
                        value = email.value,
                        onValueChange = {email.value = it},
                        label = { androidx.compose.material.Text(text = "Email", fontSize = 16.sp) })

                    Spacer(Modifier.height(6.dp))

                    TextField(
                        value = code.value,
                        onValueChange = {code.value = it},
                        label = { androidx.compose.material.Text(text = "Code", fontSize = 16.sp) })*/


                    BottomActionButtons(closeDialog, email, codigo, coroutineScope, client)

                }


            }

            Spacer(Modifier.height(8.dp))

        }
    }
}

@Composable
private fun ColumnScope.BottomActionButtons(closeDialog: () -> Unit = {}, email:  MutableState<String>, code: MutableState<String>, coroutineScope: CoroutineScope, client: Login) {

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
            onClick = {closeDialog() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xFF9300FC),
                disabledBackgroundColor = Color(0x009300FC)
            ),
            enabled = true,
            modifier = Modifier
                .weight(1f)
                .border(2.5.dp, Color(0xFFF5B041), shape = RoundedCornerShape(100.dp)),
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
            onClick = { VerifyCode(email.value, code.value, coroutineScope, client)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF5B041),
                contentColor = Color.White,
                disabledBackgroundColor = Color(0x7A9300FC)
            ),
            enabled = true,
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
}

//@Composable
//private fun OtpTextField(codeText: MutableState<String>, onOtpFieldClick:()->Unit) {
//
//
//    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
//        .fillMaxWidth()
//        .clickable(
//            interactionSource = MutableInteractionSource(),
//            indication = null
//        ) {
//            onOtpFieldClick()
//        }){
//        OtpTextFieldBox(
//            text = if (codeText.value.isNotEmpty() ) codeText.value[0].toString() else "")
//
//        OtpTextFieldBox(
//            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 2) codeText.value[1].toString() else "")
//
//        OtpTextFieldBox(
//            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 3) codeText.value[2].toString() else "")
//
//        OtpTextFieldBox(
//            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 4) codeText.value[3].toString() else "")
//
//        OtpTextFieldBox(
//            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 5) codeText.value[4].toString() else "")
//
//        OtpTextFieldBox(
//            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 6) codeText.value[5].toString() else "")
//    }
//
//}
//
//@Composable
//private fun OtpTextFieldBox(text:String) {
//
//    Box(
//        modifier = Modifier
//            .width(40.dp)
//            //.height(TextFieldDefaults.MinHeight)
//            .height(40.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .background(Color(0xFFF1F1F1)),
//        contentAlignment = Alignment.Center
//    ) {
//
//        androidx.compose.material.Text(
//            text = text,
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            color = Color.Black
//        )
//
//    }
//}



fun recoverPassword(email: String) {
    val apiUrl = BuildConfig.API_ENDPOINT
    val apiBuilder  = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = apiBuilder.create(FireStoreAPI::class.java)
    val data = BodyRequestModel(email)
    val call: Call<ResponseGenerateCode?>? = api.GenerateCode(data);

    call!!.enqueue(object: retrofit2.Callback<ResponseGenerateCode?> {
        override fun onResponse(
            call: Call<ResponseGenerateCode?>,
            response: retrofit2.Response<ResponseGenerateCode?>
        ) {
            if (response.isSuccessful) {
                //val data: ResponseGenerateCode? = response.body()
                Log.d("Respuesta: ", response.body().toString())
            }
        }

        override fun onFailure(call: Call<ResponseGenerateCode?>?, t: Throwable) {
            Log.e("Error respuesta: ", t.message.toString())
        }
    })
}

fun VerifyCode(email: String, code: String, coroutineScope: CoroutineScope, client: Login) {

    val apiUrlRecover = BuildConfig.API_ENDPOINT
    val apiBuilder  = Retrofit.Builder()
        .baseUrl(apiUrlRecover)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = apiBuilder.create(FireStoreAPI::class.java)
    val data = BodyRequestModelVerify(email, code)
    val call: Call<ResponseVerifyCode?>? = api.VerifyCode(data);
    val context = LocalContext


    call!!.enqueue(object: retrofit2.Callback<ResponseVerifyCode?> {
        override fun onResponse(
            call: Call<ResponseVerifyCode?>,
            response: retrofit2.Response<ResponseVerifyCode?>
        ) {
            if (response.isSuccessful) {
                coroutineScope.launch {
                    client.sendPasswordResetEmail(email)
                }
                Log.d("Respuesta: ", response.body().toString())

            } else {
                Log.e("Error", response.message())
            }
        }

        override fun onFailure(call: Call<ResponseVerifyCode?>?, t: Throwable) {
            //println(t.message)
            Log.e("Error respuesta: ", t.message.toString())
        }
    })
}