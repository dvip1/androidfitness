package com.pantheons.gamifiedfitness.ui.register

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
fun RegisterContent(
    onNavigateToLogin: () -> Unit, viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    LaunchedEffect(Unit) {
        Log.d("RegisterContent", "Initial auth state: $authState")
        viewModel.checkStatus()
    }
    LaunchedEffect(authState.error) {
        if (authState.error != null) {
            errorMessage = authState.error!!
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = username,
            onValueChange = { username = it.filter { char -> !char.isWhitespace() } },
            label = { Text("Username") })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email,
            onValueChange = { email = it.filter { char -> !char.isWhitespace() } },
            label = { Text("Email") })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it.filter { char -> !char.isWhitespace() } },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it.filter { char -> !char.isWhitespace() } },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation()
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
                if (password == confirmPassword) {
                    errorMessage = ""
                    viewModel.onEvent(AuthEvent.Register(email, password, username))
                } else {
                    errorMessage = "Passwords do not match"
                }
            }, enabled = !authState.isLoading
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}