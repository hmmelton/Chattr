package com.hmmelton.chattr.auth.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideFirebaseAuth() = Firebase.auth
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class GoogleServerClientId
