package com.pantheons.gamifiedfitness.ui.common.uielements

import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun  ProfileImage(imageUrl: String, alt:String="Image"){
    val context = LocalContext.current
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = alt,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
    )
}