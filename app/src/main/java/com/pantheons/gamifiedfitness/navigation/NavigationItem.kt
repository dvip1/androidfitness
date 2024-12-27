package com.pantheons.gamifiedfitness.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.AccountCircle

sealed class NavigationItem (val route: String, val icon: ImageVector, val title: String){
    object Home: NavigationItem("home", Icons.Default.Home, "Home")
    object Explore: NavigationItem("explore", Icons.Default.Search, "Explore")
    object Profile: NavigationItem("profile", Icons.Default.AccountCircle, "Profile")
    object Login: NavigationItem("login", Icons.Default.AccountCircle, "Login")
    object Register: NavigationItem("register", Icons.Default.AccountCircle, "Register")
}