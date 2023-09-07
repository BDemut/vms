package com.example.vms.visitdetails

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.editvisit.EditVisitActivity
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class VisitDetailsActivity : ComponentActivity() {
    private lateinit var visitId: String
    private val viewModel: VisitDetailsViewModel by viewModels(factoryProducer = {
        VisitDetailsViewModel.Factory(
            visitId
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitId = intent.getStringExtra(ARG_VISIT_ID) ?: throw IllegalStateException("No visit id")
        setContent {
            VisitorManagementSystemTheme {
                VisitDetailsScreen(viewModel)
            }
        }
        viewModel.events.onEach { event ->
            when (event) {
                is VisitDetailsEvent.Finish -> finish()
                is VisitDetailsEvent.NavigateToEditVisit -> launchEditVisitActivity(event.visitId)
            }
        }.launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun launchEditVisitActivity(visitId: String) =
        startActivity(EditVisitActivity.getLaunchIntent(this, visitId))

    companion object {
        private const val ARG_VISIT_ID = "visitId"

        fun getLaunchIntent(context: Context, visitId: String): Intent {
            val intent = Intent(context, VisitDetailsActivity::class.java)
            intent.putExtra(ARG_VISIT_ID, visitId)
            return intent
        }
    }
}

@Composable
fun VisitDetailsScreen(viewModel: VisitDetailsViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), color = MaterialTheme.colors.background
        ) {
            if (state.isLoading) {
                LoadingView()
            } else {
                Scaffold(
                    topBar = {
                        TopBar(
                            onDiscardClick = { viewModel.onDiscardButtonClicked() },
                            onEditClick = { viewModel.onEditButtonClicked() },
                            onChangeHostClick = { viewModel.onChangeHostButtonClicked() },
                            onCancelVisitClick = { viewModel.onCancelVisitButtonClicked() },
                            isMoreOptionsShowing = state.isMoreOptionsShowing,
                            isEditButtonShowing = state.isEditButtonShowing,
                        )
                    }
                ) {
                    VisitDetailsContent(
                        modifier = Modifier.padding(it),
                        visit = state.visit
                    )
                    if (state.isCancelVisitDialogShowing) {
                        CancelVisitDialog(
                            onConfirm = { viewModel.onCancelVisitDialogConfirmed() },
                            onDismiss = { viewModel.onCancelVisitDialogDismissed() },
                        )
                    }
                    if (state.isCancelingFailedSnackbarShowing) {
                        CancelingFailedSnackbar(
                            onDismiss = { viewModel.dismissCancelingFailedSnackbar() },
                            snackbarHostState = scaffoldState.snackbarHostState
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CancelingFailedSnackbar(onDismiss: () -> Unit, snackbarHostState: SnackbarHostState) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    LaunchedEffect("isCancelingFailedSnackbarShowing") {
        coroutineScope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                "Canceling failed"
            )
            if (snackbarResult == SnackbarResult.Dismissed) {
                onDismiss()
            }
        }
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        fontSize = 24.sp
    )
}
