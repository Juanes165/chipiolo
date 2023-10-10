package com.aragang.chipiolo.SignInChipiolo

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError : String? = null,
)
