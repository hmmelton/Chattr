package com.hmmelton.chattr.auth.di

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hmmelton.chattr.auth.data.GoogleAuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
    private const val GOOGLE_SERVER_CLIENT_ID_KEY = "GOOGLE_SERVER_CLIENT_ID"

    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @GoogleServerClientId
    fun provideGoogleServerClientId() = System.getenv(GOOGLE_SERVER_CLIENT_ID_KEY) ?: ""

    fun provideGoogleOneTapBeginSignInRequest(
        @GoogleServerClientId googleServerClientId: String
    ): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(googleServerClientId)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideGoogleAuthProvider() = GoogleAuthProvider()

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class GoogleServerClientId
