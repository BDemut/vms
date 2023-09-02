package com.example.vms.guest.home

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
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.guest.instantvisit.InstantVisitActivity
import com.example.vms.guest.ui.PinInputField
import com.example.vms.guest.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val pin = viewModel.pinValue.collectAsStateWithLifecycle().value
            VisitorManagementSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeContent(
                        pin = pin,
                        onPinChanged = { viewModel.onPinValueChanged(it) },
                        onNoPinButtonClicked = { viewModel.onNoPinButtonClicked() }
                    )
                }
            }
        }
        viewModel.events.onEach {
            when (it) {
                HomeEvent.ConfirmPin -> Unit
                HomeEvent.GoToInstantVisitCreator -> launchInstantVisitActivity()
            }
        }.launchIn(lifecycleScope)
    }

    private fun launchInstantVisitActivity() {
        startActivity(Intent(this, InstantVisitActivity::class.java))
    }
}

@Composable
fun HomeContent(
    pin: String,
    onPinChanged: (String) -> Unit,
    onNoPinButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.welcome_text),
            textAlign = TextAlign.Center
        )
        PinInputField(pinInput = pin, onPinInputChanged = { onPinChanged(it) })
        TextButton(onClick = { onNoPinButtonClicked() }) {
            Text(text = stringResource(R.string.no_pin_button_text).uppercase())
        }
    }
}

const val PIN_LENGTH = 6