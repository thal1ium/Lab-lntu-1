package com.example.lntu_app_1.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lntu_app_1.viewmodel.LoginState
import com.example.lntu_app_1.viewmodel.LoginViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onLoginSuccess()
        }
        if (state is LoginState.Error) {
            snackbarHostState.showSnackbar(
                (state as LoginState.Error).message
            )
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Login Screen", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.signIn(
                        context,
                        "209421857113-4h09dj1pqpcmqq8p5mv8u58bn79a2d3i.apps.googleusercontent.com"
                    )
                },
                enabled = state !is LoginState.Loading
            ) {
                Text("Sign in with Google")
            }
        }
    }
}