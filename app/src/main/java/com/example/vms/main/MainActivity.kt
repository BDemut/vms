package com.example.vms.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import java.util.*

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                Scaffold(
                    topBar = { HomeToolbar() }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colors.background
                    ) {
                        HomeScreen(
                            visits = visits,
                            onVisitClick = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeToolbar() {
    TopAppBar {
        IconButton(onClick = { /* navigation drawer */ }) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                contentDescription = "menu icon"
            )
        }
        Text(
            text = "Hello Janek!",
            fontWeight = FontWeight.Bold
        )
    }
}