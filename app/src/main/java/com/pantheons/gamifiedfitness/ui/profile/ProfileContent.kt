package com.pantheons.gamifiedfitness.ui.profile
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthEvent

@Composable
fun ProfileContent(viewModel: ProfileViewModel = hiltViewModel()) {
    val authState by viewModel.authState.collectAsState()
     LaunchedEffect(Unit) {
         Log.d("ProfileContent", "Initial auth state: $authState")
         viewModel.checkStatus()
     }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {viewModel.onEvent(AuthEvent.Logout)},
        ){
            Text("Logout")
        }
        Text("Profile Screen")
        Text("Profile Screen Data")
    }
}