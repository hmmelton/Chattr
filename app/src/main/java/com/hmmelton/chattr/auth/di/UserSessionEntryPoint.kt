package com.hmmelton.chattr.auth.di

import com.hmmelton.chattr.MainActivity
import com.hmmelton.chattr.auth.data.UserSession
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This is used to assist in injecting [UserSession] into [MainActivity] without field injection.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserSessionEntryPoint {
    fun userSession(): UserSession
}