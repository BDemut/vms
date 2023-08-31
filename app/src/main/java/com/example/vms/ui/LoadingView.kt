package com.example.vms.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Created by mśmiech on 31.08.2023.
 */

@Composable
fun LoadingView(withBackground: Boolean = false) {
    if (withBackground) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.Black.copy(alpha = 0.47f)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoadingIndicator()
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadingIndicator()
        }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
    )
}