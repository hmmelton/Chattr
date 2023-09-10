package com.hmmelton.chattr.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.hmmelton.chattr.auth.data.UserSession
import com.hmmelton.chattr.auth.di.UserSessionEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun rememberUserSession(): UserSession {
    val context = LocalContext.current
    val entryPoint = EntryPointAccessors.fromApplication(
        context.applicationContext,
        UserSessionEntryPoint::class.java
    )
    return rememberSaveable { entryPoint.userSession() }
}