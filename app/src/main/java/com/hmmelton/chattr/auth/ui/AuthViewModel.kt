package com.hmmelton.chattr.auth.ui

import android.content.Intent
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.hmmelton.chattr.auth.data.AuthService
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
    private val authService: AuthService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Init)
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        AuthUiState.Init
    )

    private var activeAuthJob: Job? = null

    fun onGoogleAuthClick() {
        if (activeAuthJob != null) return

        viewModelScope.launch(dispatcher) {
            _uiState.emit(AuthUiState.Loading)
            try {
                val intentSenderRequest = authService.getGoogleOneTapIntentSender()
                _uiState.emit(AuthUiState.ReadyForGoogleAuth(intentSenderRequest))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create Google One Tap IntentSenderRequest", e)
                _uiState.emit(AuthUiState.Failure(0))
            }
        }
    }

    fun processGoogleAuthResult(data: Intent?) {
        viewModelScope.launch(dispatcher) {
            try {
                val user = authService.processGoogleAuthResult(data)
                _uiState.emit(AuthUiState.Success(user))
            } catch (e: Exception) {
                Log.e(TAG, "Exception processing Google auth result", e)
                _uiState.emit(AuthUiState.Failure(0))
            }
            activeAuthJob = null
        }
    }
}

sealed class AuthUiState {
    data object Init : AuthUiState()
    data object Loading : AuthUiState()
    class Success(user: FirebaseUser) : AuthUiState()
    class Failure(@StringRes val errorRes: Int): AuthUiState()
    class ReadyForGoogleAuth(val googleIntentSender: IntentSenderRequest?): AuthUiState()
}
