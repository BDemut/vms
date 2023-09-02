package com.example.vms.guest.summary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.guest.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SummaryActivity : ComponentActivity() {

    private val viewModel: SummaryViewModel by viewModels {
        @Suppress("DEPRECATION")
        SummaryViewModelFactory(intent.getSerializableExtra(SUMMARY_TYPE_KEY) as SummaryEntryType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle().value
            VisitorManagementSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = state.description),
                            textAlign = TextAlign.Center
                        )
                        SummaryButton(
                            modifier = Modifier.padding(top = 8.dp),
                            button = state.backButton,
                            onClick = { viewModel.onReturnHomeClicked() }
                        )
                    }
                }
            }
        }
        viewModel.returnEvent.onEach {
            finishAndRemoveTask()
        }.launchIn(lifecycleScope)
    }

    companion object {
        fun createIntent(
            context: Context,
            summaryType: SummaryEntryType
        ): Intent = Intent(context, SummaryActivity::class.java)
            .putExtra(SUMMARY_TYPE_KEY, summaryType)

        const val SUMMARY_TYPE_KEY = "type"
    }

}