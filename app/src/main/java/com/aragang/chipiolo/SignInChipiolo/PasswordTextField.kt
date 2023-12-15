package com.aragang.chipiolo.SignInChipiolo
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.aragang.chipiolo.R
//
//@Composable
//fun PasswordTextField(
//    text: String,
//    modifier: Modifier = Modifier,
//    semanticContentDescription: String = "",
//    labelText: String = "",
//    validateStrengthPassword: Boolean = false,
//    hasError: Boolean = false,
//    onHasStrongPassword: (isStrong: Boolean) -> Unit = {},
//    onTextChanged: (text: String) -> Unit,
//) {
//    val focusManager = LocalFocusManager.current
//    val showPassword = remember { mutableStateOf(false) }
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .semantics { contentDescription = semanticContentDescription },
//            value = text,
//            onValueChange = onTextChanged,
//            placeholder = {
//                Text(
//                    text = labelText,
//                    color = Color.White,
//                    fontSize = 16.sp,
//                )
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                autoCorrect = true,
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = {
//                    focusManager.clearFocus()
//                }
//            ),
//            singleLine = true,
//            isError = hasError,
//            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                val (icon, iconColor) = if (showPassword.value) {
//                    Pair(
//                        Icons.Filled.Visibility,
//                        Color.Red
//                    )
//                } else {
//                    Pair(Icons.Filled.VisibilityOff, colorResource(id = R.color.colorWhite))
//                }
//
//                IconButton(onClick = { showPassword.value = !showPassword.value }) {
//                    Icon(
//                        icon,
//                        contentDescription = "Visibility",
//                        tint = iconColor
//                    )
//                }
//            },
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = Color.White,
//                unfocusedBorderColor = Color.White,
//                textColor = Color.White,
//                cursorColor = Color.White,
//            )
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        if (validateStrengthPassword && text != String.empty()) {
//            val strengthPasswordType = strengthChecker(text)
//            if (strengthPasswordType == StrengthPasswordTypes.STRONG) {
//                onHasStrongPassword(true)
//            } else {
//                onHasStrongPassword(false)
//            }
//            Text(
//                modifier = Modifier.semantics { contentDescription = "StrengthPasswordMessage" },
//                text = buildAnnotatedString {
//                    withStyle(
//                        style = SpanStyle(
//                            color = Color.White,
//                            fontSize = 10.sp,
//                            fontFamily = muliFontFamily
//                        )
//                    ) {
//                        append(stringResource(id = R.string.warning_password_level))
//                        withStyle(style = SpanStyle(color = colorResource(id = R.color.colorOrange100))) {
//                            when (strengthPasswordType) {
//                                StrengthPasswordTypes.STRONG ->
//                                    append(" ${stringResource(id = R.string.warning_password_level_strong)}")
//                                StrengthPasswordTypes.WEAK ->
//                                    append(" ${stringResource(id = R.string.warning_password_level_weak)}")
//                            }
//                        }
//                    }
//                }
//            )
//        }
//    }
//}
//
//private fun strengthChecker(password: String): StrengthPasswordTypes =
//    when {
//        REGEX_STRONG_PASSWORD.toRegex().containsMatchIn(password) -> StrengthPasswordTypes.STRONG
//        else -> StrengthPasswordTypes.WEAK
//    }
//
//enum class StrengthPasswordTypes {
//    STRONG,
//    WEAK
//}
//
//private const val REGEX_STRONG_PASSWORD =
//    "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{8,})"