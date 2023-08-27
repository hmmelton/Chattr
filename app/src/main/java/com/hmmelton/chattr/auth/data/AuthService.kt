package com.hmmelton.chattr.auth.data

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val oneTapClient: SignInClient,
    private val googleAuthProvider: GoogleAuthProvider,
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInRequest: BeginSignInRequest
) {

    suspend fun getGoogleOneTapIntentSender(): IntentSenderRequest {
        val result = oneTapClient.beginSignIn(googleSignInRequest).await()
        return IntentSenderRequest.Builder(result.pendingIntent.intentSender)
            .build()
    }

    suspend fun processGoogleAuthResult(data: Intent?): FirebaseUser {
        val idToken = getGoogleIdToken(data)
        return signInToFirebaseWithCredential(idToken)
    }

    private fun getGoogleIdToken(data: Intent?): String {
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        return googleCredential.googleIdToken
            ?: throw IllegalStateException("Google ID token null")
    }

    private suspend fun signInToFirebaseWithCredential(idToken: String): FirebaseUser {
        val firebaseCredential = googleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
        return result.user ?: throw IllegalStateException("Firebase user null")
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}