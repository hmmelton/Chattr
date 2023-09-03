package com.hmmelton.chattr.auth.data

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleOneTapAuthManager @Inject constructor(
    private val oneTapClient: SignInClient,
    private val googleSignInRequest: BeginSignInRequest
) {

    suspend fun getGoogleOneTapIntentSender(): IntentSenderRequest {
        val result = oneTapClient.beginSignIn(googleSignInRequest).await()
        return IntentSenderRequest.Builder(result.pendingIntent.intentSender)
            .build()
    }

    fun getGoogleIdToken(data: Intent?): String {
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        return googleCredential.googleIdToken
            ?: throw IllegalStateException("Google ID token null")
    }
}
