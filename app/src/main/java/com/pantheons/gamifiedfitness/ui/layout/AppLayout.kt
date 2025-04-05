package com.pantheons.gamifiedfitness.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pantheons.gamifiedfitness.ui.login.LoginContent
import com.pantheons.gamifiedfitness.ui.register.RegisterContent
import com.pantheons.gamifiedfitness.ui.usersetup.UserSetupContent
import com.pantheons.gamifiedfitness.ui.usersetup.UserSetupViewModel
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import com.pantheons.gamifiedfitness.util.common.UserSettingsManager
import kotlinx.coroutines.delay


@Composable
fun AppLayout(authUtils: AuthUtils) {
    val navController = rememberNavController()
    val authState by authUtils.authState.collectAsState()
    var loading by remember { mutableStateOf(true) }
    val isAuthenticated = authState.isAuthenticated

    // Create and remember the ViewModel
    val context = LocalContext.current
    val userSettingsManager = remember { UserSettingsManager(context) }
    val userSetupViewModel = remember { UserSetupViewModel(userSettingsManager) }
    val isSetupComplete by userSetupViewModel.isSetupComplete.collectAsState()



    // This effect will handle the loading state and navigation
    LaunchedEffect(authState, isSetupComplete) {
        // Initially show loading while checking auth state and setup status
        loading = true
        // Add a small delay to prevent flickering for very fast checks
        delay(500)
        // Auth check is complete, can hide the loading indicator
        loading = false

        // Handle navigation based on auth state and setup completion
        if (isAuthenticated) {
            if (isSetupComplete) {
                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                navController.navigate("setup") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    // Show loading or the navigation based on loading state
    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) {
                if (isSetupComplete) "home" else "setup"
            } else "login"
        ) {
            composable("register") {
                RegisterContent(
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                )
            }
            composable("login") {
                LoginContent(onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("setup") {
                UserSetupContent(
                    viewModel = userSetupViewModel,
                    onSetupComplete = {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeLayout(authUtils = authUtils)
            }
        }
    }
}