package com.hmmelton.chattr.auth.data

import com.google.firebase.auth.GoogleAuthProvider

/**
 * This class is simply a wrapper for [GoogleAuthProvider], to make dependency injection/testing
 * easier.
 */
class GoogleAuthProvider {
    fun getCredential(idToken: String?, accessToken: String?) =
        GoogleAuthProvider.getCredential(idToken, accessToken)
}