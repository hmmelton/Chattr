package com.hmmelton.chattr.auth.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmmelton.chattr.auth.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState.Init)
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        AuthUiState.Init
    )

    private var activeAuthJob: Job? = null

    fun onGoogleAuthenticationClick() {
        if (activeAuthJob != null) return

    }
}

sealed class AuthUiState {
    object Init : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    class Failure(@StringRes val errorRes: Int): AuthUiState()
}
