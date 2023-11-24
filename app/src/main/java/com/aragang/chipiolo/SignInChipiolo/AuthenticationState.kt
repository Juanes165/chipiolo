package com.aragang.chipiolo.SignInChipiolo

data class AuthenticationState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val email: String? = null,
    val password: String? = null,
    val passwordRequirements: List<PasswordRequirements> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return password?.isNotEmpty() == true &&
                email?.isNotEmpty() == true &&
                (authenticationMode == AuthenticationMode.SIGN_IN
                        || passwordRequirements.containsAll(
                    PasswordRequirements.values().toList()))
    }
}

enum class AuthenticationMode {
    SIGN_IN,
    SIGN_UP
}

enum class PasswordRequirements {
    CAPITAL_LETTER, NUMBER, EIGHT_CHARACTERS
}
