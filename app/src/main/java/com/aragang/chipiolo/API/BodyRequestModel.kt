package com.aragang.chipiolo.API

//Dataclass for generate code
data class BodyRequestModel(
    var email: String
)

data class ResponseGenerateCode(
    var id: String,
    var email: String,
    var verificationCode: String,
    var expirationTime: String
)
