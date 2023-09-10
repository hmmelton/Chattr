package com.hmmelton.chattr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.hmmelton.chattr.auth.ui.AuthScreen
import com.hmmelton.chattr.auth.ui.rememberUserSession
import com.hmmelton.chattr.navigation.MainNavHost
import com.hmmelton.chattr.theme.ChattrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChattrTheme {
                MainNavHost(
                    navController = rememberNavController(),
                    userSession = rememberUserSession()
                )
            }
        }
    }
}
