package com.pantheons.gamifiedfitness.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.ui.common.uielements.ProfileImage
import com.pantheons.gamifiedfitness.ui.common.viewmodel.AuthEvent

@Preview(showBackground = true)
@Composable
fun ProfileContent(viewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by viewModel.profileState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.checkStatus()
        viewModel.getUid()
        viewModel.getUserProfile()
    }
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
       ProfileHeaderSec(profileState)
       Button(
            onClick = { viewModel.onEvent(AuthEvent.Logout) },
        ) {
            Text("Logout")
        }
        Text("Profile Screen")
        Text("Profile Screen Data")
    }
}

@Composable
fun ProfileImageSec(profileState: ProfileResponse?=null){
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(imageUrl = profileState?.profileImage ?: "", alt = "Profile Image")
        Spacer(modifier = Modifier.height(16.dp))
        Text(("@" + profileState?.username) ?: "Loading...")
    }
}
@Composable
fun ProfileHeaderSec(profileState:ProfileResponse?=null){
    val streaks = profileState?.streaks ?: "Loading..."
    val karmas = profileState?.karmas ?: "Loading..."
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ){
        ProfileImageSec(profileState)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = androidx.compose.ui.graphics.Color.Gray),
        ) {
            HeaderDetailsLayout("Streaks", streaks.toString())
            HeaderDetailsLayout("Karmas", karmas.toString())
        }
    }
}
@Composable
fun HeaderDetailsLayout(text:String, value:String){
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(value, fontSize = 20.sp)
        Text(text)
    }
}