package com.pantheons.gamifiedfitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pantheons.gamifiedfitness.ui.layout.AppLayout
import com.pantheons.gamifiedfitness.ui.theme.GamifiedFitnessTheme
import com.pantheons.gamifiedfitness.ui.theme.Surface
import dagger.hilt.android.AndroidEntryPoint
import com.pantheons.gamifiedfitness.util.auth.AuthUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authUtils: AuthUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            GamifiedFitnessTheme {
                Surface {
                    AppLayout(authUtils = authUtils)
                }
            }
        }
    }
}