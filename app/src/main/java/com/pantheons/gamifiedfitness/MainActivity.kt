package com.pantheons.gamifiedfitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pantheons.gamifiedfitness.ui.home.HomeContent
import com.pantheons.gamifiedfitness.ui.explore.ExploreContent
import com.pantheons.gamifiedfitness.ui.profile.ProfileContent
import com.pantheons.gamifiedfitness.ui.explore.ExploreViewModel
import com.pantheons.gamifiedfitness.ui.profile.ProfileViewModel
import com.pantheons.gamifiedfitness.ui.home.HomeViewModel
import com.pantheons.gamifiedfitness.navigation.BottomNavigation
import com.pantheons.gamifiedfitness.navigation.NavigationItem
import com.pantheons.gamifiedfitness.ui.login.LoginContent
import com.pantheons.gamifiedfitness.ui.register.RegisterContent
import dagger.hilt.android.AndroidEntryPoint
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authUtils: AuthUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                App(authUtils = authUtils)
        }
    }
}

@Composable
fun App(authUtils:AuthUtils) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = "register"
    ) {
        composable("register") {
            RegisterContent(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginContent(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("home") {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) { paddingValues ->
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
                val viewModel = viewModel<ProfileViewModel>()
                val uiState by viewModel.uiState.collectAsState()
                ProfileContent(uiState = uiState)
            }
        }
    }
}
