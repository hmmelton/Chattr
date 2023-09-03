package com.hmmelton.chattr.auth.data

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserSession @Inject constructor(private val authService: AuthService) {
    private val _state = MutableStateFlow<UserSessionState>(UserSessionState.Inactive)
    val state: StateFlow<UserSessionState> = _state

    init {
        // It's fine to use GlobalScope here, since we expect UserSession to live as long as the app
        // does.
        GlobalScope.launch {
            authService.currentUser.map {  user ->
                if (user == null) {
                    UserSessionState.Inactive
                } else {
                    UserSessionState.Active(user)
                }
            }.collect {
                _state.emit(it)
            }
        }
    }

    suspend fun signInWithIdToken(idToken: String) {
        authService.signInToFirebaseWithGoogleIdToken(idToken)
    }

    fun signOut() {
        authService.signOut()
    }
}

sealed class UserSessionState {
    class Active(val user: FirebaseUser) : UserSessionState()
    data object Inactive : UserSessionState()
}
