package com.pantheons.gamifiedfitness.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.pantheons.gamifiedfitness.R
import com.pantheons.gamifiedfitness.data.remote.model.ProfileResponse
import com.pantheons.gamifiedfitness.ui.common.exercisesession.ExerciseSessionViewModel

@Preview(showBackground = true)
@Composable
fun ProfileContent(viewModel: ProfileViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileImageSec(profileState)
        ProfileHeaderSec(profileState)
        ProfileActionButtons()
        OverviewSection()
        BodyLayout(steps, hasPermissions)

    }
}

@Composable
fun ProfileImageSec(profileState: ProfileResponse? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // Green Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        )

        // Settings Button
        IconButton(
            onClick = { /* Do nothing for now */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }

        // Profile Picture
        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            // Load profile image if available
            if (profileState?.profileImage?.isNotEmpty() == true) {
                AsyncImage(
                    model = profileState.profileImage,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center),
                    tint = Color.Gray
                )
            }
        }

        // Curved bottom edge
    }
}

@Composable
fun ProfileHeaderSec(profileState: ProfileResponse? = null) {
    val streaks = profileState?.streaks ?: "Loading..."
    val karmas = profileState?.karmas ?: "Loading..."
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "@" + profileState?.username.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HeaderDetailsLayout("Level", "1")
            HeaderDetailsLayout("Streaks", streaks.toString())
            HeaderDetailsLayout("Aura", karmas.toString())
        }
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
fun BodyLayout(steps: String, hasPermissions: Boolean = false) {
    Column {
        if (hasPermissions) {
            Text("is steps loading $steps ")
        } else {
            Text("Permission is not given to me")
        }
    }
}

@Composable
fun ProfileActionButtons(
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    surfaceColor: Color = MaterialTheme.colorScheme.surface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(bottom = 10.dp, start = 16.dp, end = 16.dp)),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { /* Handle add friends action */ }, modifier = Modifier
                .weight(0.85f)
                .shadow(
                    elevation = 4.dp, shape = RoundedCornerShape(20.dp)
                ), colors = ButtonDefaults.outlinedButtonColors(
                containerColor = surfaceColor, contentColor = primaryColor
            ), border = BorderStroke(1.dp, primaryColor), shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person, contentDescription = null
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Friends", fontWeight = FontWeight.Medium
                )
            }
        }

        IconButton(
            onClick = { /* Handle share action */ }, modifier = Modifier
                .weight(0.15f)
                .shadow(
                    elevation = 4.dp, shape = CircleShape
                )
                .background(
                    color = surfaceColor, shape = CircleShape
                )
                .border(
                    width = 1.dp, color = primaryColor, shape = CircleShape
                )
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share, contentDescription = "Share", tint = primaryColor
            )
        }
    }
}

@Composable
fun OverviewSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Heading
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Grid of stat cards - 2x2 layout
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.trophy_solid, // Replace with your icon resource
                score = "15",
                label = "Day Streak"
            )

            StatCard(
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.trophy_solid, // Replace with your icon resource
                score = "2,450",
                label = "Total XP"
            )

        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.trophy_solid, // Replace with your icon resource
                score = "4",
                label = "League"
            )

            StatCard(
                modifier = Modifier.weight(1f),
                iconResId = R.drawable.trophy_solid, // Replace with your icon resource
                score = "23",
                label = "Lessons"
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier, iconResId: Int, score: String, label: String
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(16.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon on the left
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = score,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}