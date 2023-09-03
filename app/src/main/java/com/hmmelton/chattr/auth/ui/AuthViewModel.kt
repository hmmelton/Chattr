package com.hmmelton.chattr.auth.ui

import android.content.Intent
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmmelton.chattr.R
import com.hmmelton.chattr.auth.data.GoogleOneTapAuthManager
import com.hmmelton.chattr.auth.data.UserSession
import com.hmmelton.chattr.auth.data.UserSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userSession: UserSession,
    private val oneTapAuthManager: GoogleOneTapAuthManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Init)
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        AuthUiState.Init
    )

    private var activeAuthJob: Job? = null

    init {
        viewModelScope.launch {
            emitSuccessStateWhenUserSessionBecomesActive()
        }
    }

    private suspend fun emitSuccessStateWhenUserSessionBecomesActive() {
        userSession.state.collect { sessionState ->
            if (sessionState is UserSessionState.Active) {
                _uiState.emit(AuthUiState.Success)
            }
        }
    }

    fun onGoogleAuthClick() {
        if (activeAuthJob != null) return

        viewModelScope.launch(dispatcher) {
            _uiState.emit(AuthUiState.Loading)
            try {
                val intentSenderRequest = oneTapAuthManager.getGoogleOneTapIntentSender()
                _uiState.emit(AuthUiState.ReadyForGoogleAuth(intentSenderRequest))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create Google One Tap IntentSenderRequest", e)
                _uiState.emit(AuthUiState.Failure(R.string.login_error_message))
            }
        }
    }

    fun processGoogleAuthResult(data: Intent?) {
        viewModelScope.launch(dispatcher) {
            try {
                val token = oneTapAuthManager.getGoogleIdToken(data)
                userSession.signInWithIdToken(token)
            } catch (e: Exception) {
                Log.e(TAG, "Exception processing Google auth result", e)
                _uiState.emit(AuthUiState.Failure(R.string.login_error_message))
            }
            activeAuthJob = null
        }
    }
}

sealed class AuthUiState {
    data object Init : AuthUiState()
    data object Loading : AuthUiState()
    data object Success : AuthUiState()
    class Failure(@StringRes val errorRes: Int): AuthUiState()
    class ReadyForGoogleAuth(val googleIntentSender: IntentSenderRequest?): AuthUiState()
}
