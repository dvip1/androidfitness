package com.pantheons.gamifiedfitness.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pantheons.gamifiedfitness.navigation.BottomNavigation
import com.pantheons.gamifiedfitness.navigation.NavigationItem
import com.pantheons.gamifiedfitness.ui.common.uielements.CreateCommunityDialog
import com.pantheons.gamifiedfitness.ui.explore.ExploreContent
import com.pantheons.gamifiedfitness.ui.explore.ExploreViewModel
import com.pantheons.gamifiedfitness.ui.home.HomeContent
import com.pantheons.gamifiedfitness.ui.home.HomeViewModel
import com.pantheons.gamifiedfitness.ui.profile.ProfileContent
import com.pantheons.gamifiedfitness.ui.profile.ProfileViewModel
import com.pantheons.gamifiedfitness.util.auth.AuthUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(authUtils: AuthUtils) {
    val authState by authUtils.authState.collectAsState()
    val navController = rememberNavController()
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Gamified Fitness") }, actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Favorite")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Logout") }, onClick = { authUtils.logout() })
                DropdownMenuItem(text = { Text("create community") },
                    onClick = { showDialog = true })
            }
        })
    }, bottomBar = { BottomNavigation(navController = navController) }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationItem.Home.route) {
                val viewModel = viewModel<HomeViewModel>()
                val uiState by viewModel.uiState.collectAsState()
                HomeContent(uiState = uiState)
            }

            composable(NavigationItem.Explore.route) {
                val viewModel = viewModel<ExploreViewModel>()
                val uiState by viewModel.uiState.collectAsState()
                ExploreContent(uiState = uiState)
            }

            composable(NavigationItem.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileContent(viewModel = viewModel)
            }
        }
        CreateCommunityDialog(showDialog = showDialog,
            onDismissRequest = { showDialog = false },
            onConfirm = { name, description, rules, isPrivate ->
                showDialog = false
                itemName = name
                println("Name: $name, Description: $description, Rules: $rules, Private: $isPrivate")
            })
    }
}
