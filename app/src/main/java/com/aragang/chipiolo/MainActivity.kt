package com.aragang.chipiolo

import OtpTextFieldScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aragang.chipiolo.CreateUserScreen.CreateUserScreen
import com.aragang.chipiolo.Home.FirstScreen
import com.aragang.chipiolo.Home.HomeScreen
import com.aragang.chipiolo.Profile.ProfileHome
import com.aragang.chipiolo.ProfilePicUpdate.CameraScreen
import com.aragang.chipiolo.SignInChipiolo.Login
import com.aragang.chipiolo.SignInChipiolo.LoginScreen
import com.aragang.chipiolo.SignInChipiolo.RecoverScreen
import com.aragang.chipiolo.Profile.ProfileScreen
import com.aragang.chipiolo.SignInChipiolo.SignInViewModel
import com.aragang.chipiolo.ui.theme.ChipioloTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        Login(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val you_view: TabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChipioloTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "first_screen") {

                        // FIRST SCREEN - LOGO CHIPIOLO
                        composable("first_screen") {
                            // Logo chipiolo
                            FirstScreen()
                            // Revisar si esta logeado y redirigir
                            LaunchedEffect(key1 = Unit) {
                                if(googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("home")
                                } else {
                                    navController.navigate("login_screen")
                                }
                            }
                        }

                        // HOME SCREEN - BOTONES DE UN JUGADOR Y MULTIJUGADOR
                        composable("home") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            HomeScreen(
                                onProfile = {navController.navigate("profile_home")},
                                onSinglePlayer = {navController.navigate("game")},
                                onMultiPlayer = {navController.navigate("game")},
                            )
                        }

                        // PROFILE HOME - PERFIL Y ESTADISTICAS
                        composable("profile_home") {
                            ProfileHome(
                                viewModel = you_view,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.popBackStack()
                                    }
                                },
                                onPlay = {navController.navigate("game")},
                                onProfile = {navController.navigate("profile")},
                            )
                        }

                        // PERFIL DEL USUARIO
                        composable("profile") {
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("login_screen")
                                    }
                                },
                                onCamera = {
                                    navController.navigate("camera")
                                }
                            )
                        }

                        composable("verify_code") {
                            OtpTextFieldScreen(client = googleAuthUiClient)
                        }

                        // PANTALLA DE LOGIN
                        composable("login_screen"){
                            LoginScreen(
                                client = googleAuthUiClient,
                                onSuccess = {
                                    navController.navigate("home")
                                },
                                onRegister = {
                                    navController.navigate("create_user")
                                },
                                onRecoverPassword = {
                                    navController.navigate("recover_password")
                                }
                            )
                        }

                        // PANTALLA DE REGISTRO
                        composable("create_user"){
                            CreateUserScreen(
                                client = googleAuthUiClient
                            )
                        }

                        // PANTALLA DE RECUPERAR CONTRASEÑA
                        composable("recover_password") {
                            RecoverScreen(
                                onGoBack = {
                                    navController.popBackStack()
                                },
                                onCodeSent = {
                                    navController.navigate("verify_code")
                                }
                            )
                        }

                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("Home")
                                    viewModel.resetState()
                                }
                            }
                        }

                        composable("camera") {
                            CameraScreen()
                        }



                        // GAME SCREEN - JUEGO
                        composable("game") {
                            GameScreen()
                        }
                    }
                }
            }
        }
    }
}