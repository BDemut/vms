package com.example.vms.auditlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuditLogActivity : ComponentActivity() {
    private val viewModel: AuditLogViewModel by viewModels(factoryProducer = { AuditLogViewModel.Factory() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                AuditLogScreen(viewModel)
            }
        }
        viewModel.events.onEach { event ->
            when (event) {
                is AuditLogEvent.Finish -> finish()
            }
        }.launchIn(lifecycleScope)
    }
}

@Composable
fun AuditLogScreen(viewModel: AuditLogViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        onBackPressed = viewModel::onBackPressed,
                    )
                },
                bottomBar = {
                    BottomBar(
                        onGenerateClick = viewModel::onGenerateClicked
                    )
                }
            ) {
                AuditLogContent(
                    modifier = Modifier.padding(it),
                    state = state,
                    onStartDateChange = viewModel::changeStartDate,
                    onStartTimeChange = viewModel::changeStartTime,
                    onEndDateChange = viewModel::changeEndDate,
                    onEndTimeChange = viewModel::changeEndTime,
                )
                if (state.isGenerationFailedSnackbarShowing) {
                    GenerationFailedSnackbar(
                        onDismiss = { viewModel.dismissGenerationFailedSnackbar() },
                        snackbarHostState = scaffoldState.snackbarHostState
                    )
                }
                if (state.isGenerationSucceedDialogShowing) {
                    GenerationSucceedDialog(
                        onDismissDialog = { viewModel.onGenerationSucceedDialogConfirmed() },
                        signInUserEmail = state.signInUserEmail
                    )
                }
            }
        }
    }
    if (state.isProcessing) {
        LoadingView(withBackground = true)
    }
}

@Composable
private fun GenerationFailedSnackbar(onDismiss: () -> Unit, snackbarHostState: SnackbarHostState) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val text = stringResource(R.string.generation_failed_snackbar)
    LaunchedEffect("isGenerationFailedSnackbarShowing") {
        coroutineScope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(text)
            if (snackbarResult == SnackbarResult.Dismissed) {
                onDismiss()
            }
        }
    }
}