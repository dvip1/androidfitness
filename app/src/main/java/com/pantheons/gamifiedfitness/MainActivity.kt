package com.pantheons.gamifiedfitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pantheons.gamifiedfitness.ui.layout.AppLayout
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
                AppLayout(authUtils = authUtils)
        }
    }
}