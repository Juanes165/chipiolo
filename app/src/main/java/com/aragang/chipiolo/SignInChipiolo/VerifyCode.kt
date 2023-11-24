import android.app.ActionBar
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.aragang.chipiolo.API.BodyRequestModel
import com.aragang.chipiolo.API.BodyRequestModelVerify
import com.aragang.chipiolo.API.FireStoreAPI
import com.aragang.chipiolo.API.ResponseGenerateCode
import com.aragang.chipiolo.API.ResponseVerifyCode
import com.aragang.chipiolo.SignInChipiolo.Login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpTextFieldScreen(client: Login) {

    val coroutineScope = rememberCoroutineScope()
    val codeTxtFieldTxt = remember { mutableStateOf("") }
    val textFieldRequester = remember { FocusRequester() }
    val email = remember { mutableStateOf("") }
    val code = remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color.Blue, Color(0xFF9500FF)),
                    startX = -600f,
                    endX = 600f
                )
            )
            .imePadding()
    ) {

        ScreenTitle()
        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                .background(Color.White)
                .padding(end = 18.dp, start = 18.dp, top = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            Box (modifier=Modifier.weight(1f), contentAlignment = Alignment.Center){

                TextField(
                    value = codeTxtFieldTxt.value,
                    onValueChange = {
                        if (it.length <= 4) {
                            codeTxtFieldTxt.value = it
                            if(it.length == 4){
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
                )

                Column{
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Por favor revise su correo",
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(18.dp))

                    OtpTextField(codeText = codeTxtFieldTxt) {
                        focusManager.clearFocus()
                        textFieldRequester.requestFocus()
                    }


                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = email.value,
                        onValueChange = {email.value = it},
                        label = { Text(text = "Email", fontSize = 16.sp) })

                    Spacer(Modifier.height(6.dp))

                    TextField(
                        value = code.value,
                        onValueChange = {code.value = it},
                        label = { Text(text = "Code", fontSize = 16.sp) })


                    BottomActionButtons(email, code, coroutineScope, client)

                }


            }

            Spacer(Modifier.height(8.dp))

        }

    }
}

@Composable
private fun ColumnScope.BottomActionButtons(email: MutableState<String>, code: MutableState<String>, coroutineScope: CoroutineScope, client: Login){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .weight(1f),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xFF9300FC),
                disabledBackgroundColor = Color(0x009300FC)
            ),
            enabled = true,
            modifier = Modifier
                .weight(1f)
                .border(2.5.dp, Color(0xFF9500FF), shape = RoundedCornerShape(100.dp)),
            shape = RoundedCornerShape(100.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Cancel ?"
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Cancelar",
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 6.dp, top = 6.dp, bottom = 6.dp)
            )

        }

        Spacer(Modifier.width(6.dp))

        Button(
            onClick = { VerifyCode(email.value, code.value, coroutineScope, client)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF9500FF),
                contentColor = Color.White,
                disabledBackgroundColor = Color(0x7A9300FC)
            ),
            enabled = true,
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier.weight(1f),

            ) {

            Text(
                "Verificar",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            )

        }


    }


}


@Composable
private fun ScreenTitle(){

    Box(
        Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {

        val title = buildAnnotatedString {

            append("Chipiolo ")
            this.addStyle(
                SpanStyle(color = Color.White, fontFamily = FontFamily.SansSerif),
                0,
                4
            )
            append("code Verification")
            this.addStyle(
                SpanStyle(color = Color.Yellow, fontFamily = FontFamily.SansSerif),
                4,
                14
            )

        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                "Coloque el código para continuar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

        }
    }
}

@Composable
private fun OtpTextField(codeText: MutableState<String>, onOtpFieldClick:()->Unit) {


    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            onOtpFieldClick()
        }){
        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() ) codeText.value[0].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 2) codeText.value[1].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 3) codeText.value[2].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 4) codeText.value[3].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 5) codeText.value[4].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 6) codeText.value[5].toString() else "")
    }

}

@Composable
private fun OtpTextFieldBox(text:String) {

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(TextFieldDefaults.MinHeight)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F1F1)),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

    }
}

fun VerifyCode(email: String, code: String, coroutineScope: CoroutineScope, client: Login) {

    val apiBuilder  = Retrofit.Builder()
        .baseUrl("https://90fe-181-234-146-197.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = apiBuilder.create(FireStoreAPI::class.java)
    val data = BodyRequestModelVerify(email, code)
    val call: Call<ResponseVerifyCode?>? = api.VerifyCode(data);

    call!!.enqueue(object: retrofit2.Callback<ResponseVerifyCode?> {
        override fun onResponse(
            call: Call<ResponseVerifyCode?>,
            response: retrofit2.Response<ResponseVerifyCode?>
        ) {
            if (response.isSuccessful()) {
                coroutineScope.launch {
                    client.sendPasswordResetEmail(email)
                }
                Log.d("Respuesta: ", response.body().toString())
            } else {
                Log.e("No estas autorizado", "El Codigo que has proporcionado no es correcto")
            }
        }

        override fun onFailure(call: Call<ResponseVerifyCode?>?, t: Throwable) {
            //println(t.message)
            Log.e("Error respuesta: ", t.message.toString())
        }
    })
}