package com.aragang.chipiolo.SignInChipiolo

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.aragang.chipiolo.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
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
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }


    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        var result: SignInResult
        try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            Log.d("Login", "signInWithEmailAndPassword: $user")


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
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            result = SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
        return result
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): SignInResult {

        var result: SignInResult
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
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
            FirebaseFirestore.getInstance().collection("UserData").document(user?.uid.toString())
                .get().addOnSuccessListener {
                if (!it.exists()) {
                    val newUser = hashMapOf(
                        "name" to user?.displayName,
                        "email" to user?.email,
                        "profileImage" to user?.photoUrl.toString(),
                        "uid" to user?.uid
                    )
                    FirebaseFirestore.getInstance().collection("UserData")
                        .document(user?.uid.toString()).set(newUser)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            result = SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
        return result
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
            FirebaseFirestore.getInstance().collection("UserData").document(user?.uid.toString())
                .get().addOnSuccessListener {
                if (!it.exists()) {
                    val newUser = hashMapOf(
                        "name" to user?.displayName,
                        "email" to user?.email,
                        "profileImage" to user?.photoUrl.toString(),
                        "uid" to user?.uid
                    )
                    FirebaseFirestore.getInstance().collection("UserData")
                        .document(user?.uid.toString()).set(newUser)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
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
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Boolean {
        var result = false
        try {
            auth.sendPasswordResetEmail(email).await()
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
        return result
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

    // Update the email in Firebase Auth
    fun updateEmail(email: String) {
        val user = auth.currentUser ?: return
        Log.d("FIRELOGIN", "Provider" + user.providerId)
        // Verify email before updating
        user.verifyBeforeUpdateEmail(email)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    Log.d("Login", "verifyBeforeUpdateEmail: success")
                    user.updateEmail(email)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("Login", "updateEmail: success")
                            } else {
                                Log.e("Login", "updateEmail: ${it.exception?.message}")
                            }
                        }
                } else {
                    Log.e("Login", "verifyBeforeUpdateEmail: ${it.exception?.message}")
                }
            }

    }

    // Update the user name in Firebase Auth
    fun updateName(name: String) {
        val user = auth.currentUser ?: return
        user.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        )
    }

    // Update the photo url in Firebase Auth
    fun updatePhotoUrl(photoUrl: String) {
        val user = auth.currentUser ?: return
        user.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUrl))
                .build()
        )
    }

    fun deleteAccount() {
        auth.currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                return@addOnCompleteListener
            } else {
                Log.e("Login", "deleteAccount: ${task.exception?.message}")
            }
        }?.addOnFailureListener() { exception ->
            Log.e("Login", "deleteAccount: ${exception.message}")
            return@addOnFailureListener
        }
    }

    companion object
}