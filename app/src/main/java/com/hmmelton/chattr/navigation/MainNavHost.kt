package com.hmmelton.chattr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hmmelton.chattr.auth.data.UserSession
import com.hmmelton.chattr.auth.data.UserSessionState
import com.hmmelton.chattr.auth.ui.AuthScreen
import com.hmmelton.chattr.auth.ui.AuthViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    userSession: UserSession
) {
    val startDestination = rememberStartDestination(userSession)
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.HOME) {
            // TODO(Home page needs to be built first)
        }
        composable(Routes.AUTH) {
            val viewModel = hiltViewModel<AuthViewModel>()
            AuthScreen(viewModel = viewModel)
        }
    }

    NavigateToAuthScreenOnInactiveSession(navController, userSession)
}

@Composable
private fun rememberStartDestination(userSession: UserSession): String {
    val initialAuthState = userSession.state.collectAsState().value
    return rememberSaveable {
        if (initialAuthState is UserSessionState.Active) Routes.HOME else Routes.AUTH
    }
}

@Composable
private fun NavigateToAuthScreenOnInactiveSession(
    navController: NavHostController,
    userSession: UserSession
) {
    LaunchedEffect(userSession) {
        userSession.state.collect { newState ->
            if (newState is UserSessionState.Inactive) {
                navController.navigate(Routes.AUTH) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
            }
        }
    }
}
