package com.aragang.chipiolo.API

import retrofit2.Call
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
}