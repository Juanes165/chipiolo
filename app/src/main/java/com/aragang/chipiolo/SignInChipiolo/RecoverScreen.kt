package com.aragang.chipiolo.SignInChipiolo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.API.BodyRequestModel
import com.aragang.chipiolo.API.FireStoreAPI
import com.aragang.chipiolo.API.ResponseGenerateCode
import com.aragang.chipiolo.R
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//@Preview
@Composable
fun RecoverScreen(
    //navController: NavController
) {
    var emailRecover = remember { mutableStateOf("") }

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
                label = { Text(text = "Email", fontSize = 16.sp) },
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

            Button(onClick = { recoverPassword(emailRecover.value) }) {
                Text(text = stringResource(R.string.send_recover))
            }
        }
    }
}


fun recoverPassword(email: String) {
    val apiBuilder  = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:3000")
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
            if (response.isSuccessful()) {
                //val data: ResponseGenerateCode? = response.body()
                //println(data)
                Log.d("Respuesta: ", response.body().toString())
            }
        }

        override fun onFailure(call: Call<ResponseGenerateCode?>?, t: Throwable) {
            //println(t.message)
            Log.e("Error respuesta: ", t.message.toString())
        }
    })
}