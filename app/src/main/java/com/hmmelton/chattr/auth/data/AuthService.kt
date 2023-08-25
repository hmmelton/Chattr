package com.hmmelton.chattr.auth.data

import com.google.android.gms.auth.api.identity.SignInClient
import com.hmmelton.chattr.auth.di.GoogleServerClientId
import javax.inject.Inject

class AuthService @Inject constructor(
    private val oneTapClient: SignInClient,
    @GoogleServerClientId private val googleServerClientId: String
) {
}