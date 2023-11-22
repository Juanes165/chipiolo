package com.aragang.chipiolo.SignInChipiolo

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.aragang.chipiolo.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


class Login(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        var result: SignInResult
        try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            result = SignInResult(
                data = user?.run {
                    UserData(
                        id = uid,
                        name = displayName,
                        profileImage = photoUrl?.toString(),
                        email = email ?: ""
                    )
                },
                errorMessage = null
            )
            FirebaseFirestore.getInstance().collection("UserData").document(user?.uid.toString()).get().addOnSuccessListener {
                if (!it.exists()) {
                    val newUser = hashMapOf(
                        "name" to user?.displayName,
                        "email" to user?.email,
                        "profileImage" to user?.photoUrl.toString(),
                        "uid" to user?.uid
                    )
                    FirebaseFirestore.getInstance().collection("UserData").document(user?.uid.toString()).set(newUser)
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            result = SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
        return result
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            id = uid,
            name = displayName,
            profileImage = photoUrl?.toString(),
            email = email ?: ""
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.server_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    companion object
}