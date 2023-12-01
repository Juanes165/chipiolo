package com.aragang.chipiolo.API

import android.util.Log
import com.aragang.chipiolo.SignInChipiolo.Login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

public interface FireStoreAPI {
    @Headers(
        "Accept: application/json"
    )
    @POST("users/generate-code")
    fun GenerateCode(@Body bdrequest: BodyRequestModel?): Call<ResponseGenerateCode?>?

    @POST("users/verify-code")
    fun VerifyCode(@Body bdrequest: BodyRequestModelVerify?): Call<ResponseVerifyCode?>?
}

public fun VerifyCode(
    email: String,
    code: String,
    coroutineScope: CoroutineScope,
    client: Login,
    openExito: () -> Unit = {},
    openError: () -> Unit = {}
) {

    //val apiUrlRecover = BuildConfig.API_ENDPOINT
    val apiUrlRecover = "https://chipioloapi.vercel.app/"
    val apiBuilder = Retrofit.Builder()
        .baseUrl(apiUrlRecover)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = apiBuilder.create(FireStoreAPI::class.java)
    val data = BodyRequestModelVerify(email, code)
    val call: Call<ResponseVerifyCode?>? = api.VerifyCode(data);



    call!!.enqueue(object : retrofit2.Callback<ResponseVerifyCode?> {
        override fun onResponse(
            call: Call<ResponseVerifyCode?>,
            response: retrofit2.Response<ResponseVerifyCode?>
        ) {
            if (response.isSuccessful) {
                coroutineScope.launch {
                    client.sendPasswordResetEmail(email)

                }
                Log.d("Respuesta: ", response.body().toString())
                openExito()
            } else {
                Log.e("Error", response.message())
                openError()
            }
        }

        override fun onFailure(call: Call<ResponseVerifyCode?>?, t: Throwable) {
            //println(t.message)
            Log.e("Error respuesta: ", t.message.toString())
        }
    })
}


public fun recoverPassword(email: String) {
    //val apiUrl = BuildConfig.API_ENDPOINT
    val apiUrl = "https://chipioloapi.vercel.app/"
    val apiBuilder = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = apiBuilder.create(FireStoreAPI::class.java)
    val data = BodyRequestModel(email)
    val call: Call<ResponseGenerateCode?>? = api.GenerateCode(data);

    call!!.enqueue(object : retrofit2.Callback<ResponseGenerateCode?> {
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