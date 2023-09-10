@file:OptIn(ExperimentalMaterial3Api::class)

package com.hmmelton.chattr.auth.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmmelton.chattr.R
import com.hmmelton.chattr.theme.ChattrTheme

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        viewModel.processGoogleAuthResult(result.data)
    }
    if (uiState is AuthUiState.ReadyForGoogleAuth) {
        val intentSenderRequest = (uiState as AuthUiState.ReadyForGoogleAuth).googleIntentSender
        launcher.launch(intentSenderRequest)
    }

    AuthScreen(
        uiState = uiState,
        onEmailSignInClick = { /* TODO */ },
        onGoogleAuthClick = { viewModel.onGoogleAuthClick() }
    )
}

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onEmailSignInClick: () -> Unit,
    onGoogleAuthClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.screen_padding)),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayMedium
            )
            SignInOptions(onEmailSignInClick, onGoogleAuthClick)
        }
    }
}

@Composable
fun SignInOptions(
    onEmailSignInClick: () -> Unit,
    onGoogleAuthClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EmailAndPasswordSection()
        SignInButton(onEmailSignInClick)
        SignInProviderDivider()
        SignInProviderSection(onGoogleAuthClick)
    }
}

@Composable
fun EmailAndPasswordSection() {
    var emailText: String by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.double_standard_padding)),
        value = emailText,
        placeholder = { Text(stringResource(R.string.email_placeholder)) },
        onValueChange = { newValue -> emailText = newValue }
    )

    var passwordText: String by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.double_standard_padding)),
        value = passwordText,
        placeholder = { Text(stringResource(R.string.password_placeholder)) },
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = { newValue -> passwordText = newValue }
    )
}

@Composable
fun SignInButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.standard_padding)),
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.sign_in_button),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SignInProviderDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.double_standard_padding)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
        Text(
            text = stringResource(R.string.sign_in_provider_divider_label),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.quadruple_standard_padding))
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}

@Composable
fun SignInProviderSection(onGoogleAuthClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.standard_padding)),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        onClick = onGoogleAuthClick
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(R.drawable.google_logo), // Replace with your image resource
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.sign_in_with_google_label),
                style = MaterialTheme.typography.bodyLarge
            )
            Box(modifier = Modifier.width(24.dp))
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AuthScreenPreview() {
    ChattrTheme {
        AuthScreen(
            uiState = AuthUiState.Init,
            onEmailSignInClick = {},
            onGoogleAuthClick = {}
        )
    }
}
