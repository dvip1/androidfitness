package com.pantheons.gamifiedfitness.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.ui.common.uielements.ProfileImage
import com.pantheons.gamifiedfitness.ui.common.exercisesession.ExerciseSessionViewModel
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.PermissionController

@Preview(showBackground = true)
@Composable

fun ProfileContent(viewModel: ProfileViewModel = hiltViewModel()) {
    val exerciseSessionViewModel: ExerciseSessionViewModel = hiltViewModel()
    val profileState by viewModel.profileState.collectAsState()
    val isLoadingSteps = exerciseSessionViewModel.isLoadingTodaySteps.collectAsState().value
    val todaySteps = exerciseSessionViewModel.todayStepsData.value
    var hasPermissions by remember { mutableStateOf(false) }


    val steps: String = if (isLoadingSteps) {
        "Loading..."
    } else {
        todaySteps?.steps?.toString() ?: "No steps data" // added null safety for steps.
    }

    LaunchedEffect(Unit) {
        viewModel.checkStatus()
        viewModel.getUid()
        viewModel.getUserProfile()
    }
   LaunchedEffect(Unit) {
       hasPermissions = exerciseSessionViewModel.hasAllPermission()
       exerciseSessionViewModel.initialLoad()
       exerciseSessionViewModel.loadTodaySteps()
   }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeaderSec(profileState)
        BodyLayout(steps, hasPermissions)
    }
}

@Composable
fun ProfileImageSec(profileState: ProfileResponse? = null) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(imageUrl = profileState?.profileImage ?: "", alt = "Profile Image")
        Spacer(modifier = Modifier.height(16.dp))
        Text(("@" + profileState?.username))
    }
}

@Composable
fun ProfileHeaderSec(profileState: ProfileResponse? = null) {
    val streaks = profileState?.streaks ?: "Loading..."
    val karmas = profileState?.karmas ?: "Loading..."
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImageSec(profileState)
        HeaderDetailsLayout("Level", "1")
        HeaderDetailsLayout("Streaks", streaks.toString())
        HeaderDetailsLayout("Aura", karmas.toString())
    }
}

@Composable
fun HeaderDetailsLayout(text: String, value: String) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 20.sp)
        Text(text)
    }
}
@Composable
fun BodyLayout(steps:String, hasPermissions:Boolean = false){
    Column {
        if(hasPermissions) {
            Text("is steps loading $steps ")
        }
        else {
            Text("Permission is not given to me")
        }
    }
}