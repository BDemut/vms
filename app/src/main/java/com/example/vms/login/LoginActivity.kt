package com.example.vms.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.home.HomeActivity
import com.example.vms.home.HomeEvent
import com.example.vms.settings.SettingsActivity
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * Created by mśmiech on 16.08.2023.
 */
class LoginActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                LoginScreen(loginViewModel)
            }
        }
        loginViewModel.events.onEach { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> launchHomeActivity()
            }
        }.launchIn(lifecycleScope)
    }

    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginTextField()
            PasswordTextField()
            TextButton(onClick = { viewModel.onLoginButtonClicked() }) {
                Text(stringResource(R.string.login))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginTextField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = { Text(text = "Login") },
        placeholder = { Text(text = "Enter your login") },
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTextField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var showPassword by remember { mutableStateOf(value = false) }
    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Enter your password") },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "hide_password"
                    )
                }
            } else {
                IconButton(
                    onClick = { showPassword = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "hide_password"
                    )
                }
            }
        }
    )
}