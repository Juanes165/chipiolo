package com.aragang.chipiolo.SignInChipiolo;

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val id: String,
    val name: String?,
    val email: String,
    val profileImage: String?
)

