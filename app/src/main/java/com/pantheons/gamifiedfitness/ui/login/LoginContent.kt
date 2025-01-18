package com.pantheons.gamifiedfitness.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthEvent
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthViewModel

@Composable
fun LoginContent(
    onNavigateToRegister: () -> Unit, viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        Log.d("LoginContent", "Auth state changed - Full state: $authState")
        viewModel.checkStatus()
    }

    LaunchedEffect(authState.error) {
        errorMessage = authState.error ?: ""
    }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email,
            onValueChange = { email = it.filter { char -> !char.isWhitespace() } },
            label = { Text("Email") },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it.filter { char -> !char.isWhitespace() } },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                Log.d("LoginContent", "Login button clicked")
                viewModel.onEvent(AuthEvent.Login(email, password))
                Log.d("LoginContent", "Login event sent")
                Log.d("LoginContent", "Auth state after login event: ${authState}")
            }, enabled = !authState.isLoading, modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (authState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onNavigateToRegister, modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Don't have an account? Register")
        }
    }
}