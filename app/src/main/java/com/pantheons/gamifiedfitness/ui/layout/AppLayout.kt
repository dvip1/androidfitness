package com.pantheons.gamifiedfitness.ui.layout

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pantheons.gamifiedfitness.ui.login.LoginContent
import com.pantheons.gamifiedfitness.ui.register.RegisterContent
import com.pantheons.gamifiedfitness.util.auth.AuthUtils

@Composable
fun AppLayout(authUtils: AuthUtils) {
    val navController = rememberNavController()
    val authState by authUtils.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        Log.d("App", "Auth state changed: isAuthenticated=${authState.isAuthenticated}")
        if (authState.isAuthenticated) {
            Log.d("App", "Navigating to home")
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) "home" else "login"
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

        composable("home") {
            HomeLayout(authUtils = authUtils)
        }
    }
}
