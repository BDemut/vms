package com.example.vms.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.home.HomeActivity
import com.example.vms.ui.LoadingView
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
                is LoginEvent.NavigateToHome -> {
                    launchHomeActivity()
                    finish()
                }

                is LoginEvent.ShowLoginError -> showLoginErrorToast(event.messageResId)
            }
        }.launchIn(lifecycleScope)
    }

    private fun launchHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun showLoginErrorToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.onStart()
    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LoginContent(state = state,
            onUsernameChange = { viewModel.setUsername(it) },
            onPasswordChange = { viewModel.setPassword(it) },
            onLoginButtonClicked = { viewModel.onLoginButtonClicked() }
        )
        if (state.isLoading) {
            LoadingView(withBackground = true)
        }
    }
}

@Composable
fun LoginContent(
    state: LoginState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Username(
            username = state.username,
            onUsernameChange = onUsernameChange,
            isUsernameValid = state.isUsernameValid
        )
        Spacer(modifier = Modifier.height(8.dp))
        Password(
            password = state.password,
            onPasswordChange = onPasswordChange,
            isPasswordValid = state.isPasswordValid
        )
        TextButton(onClick = onLoginButtonClicked) {
            Text(stringResource(R.string.login))
        }
    }
}

@Composable
fun Username(
    username: String,
    onUsernameChange: (String) -> Unit,
    isUsernameValid: Boolean
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = username,
        onValueChange = { onUsernameChange(it) },
        label = { Text(text = stringResource(id = R.string.username_label)) },
        placeholder = { Text(text = stringResource(id = R.string.username_placeholder)) },
        isError = !isUsernameValid,
    )
    if (!isUsernameValid) {
        TextFieldError(stringResource(id = R.string.username_invalid))
    }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.error
        )
    }
}

@Composable
fun Password(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordValid: Boolean
) {
    var showPassword by remember { mutableStateOf(value = false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text(text = stringResource(id = R.string.password_label)) },
        placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
        isError = !isPasswordValid,
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            ShowPasswordIcon(showPassword = showPassword,
                onClick = {
                    showPassword = it
                })
        }
    )
    if (!isPasswordValid) {
        TextFieldError(stringResource(id = R.string.password_invalid))
    }
}

@Composable
fun ShowPasswordIcon(showPassword: Boolean, onClick: (Boolean) -> Unit) {
    if (showPassword) {
        IconButton(onClick = { onClick(false) }) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = stringResource(id = R.string.hide_password_icon_content_description)
            )
        }
    } else {
        IconButton(
            onClick = { onClick(true) }) {
            Icon(
                imageVector = Icons.Filled.VisibilityOff,
                contentDescription = stringResource(id = R.string.hide_password_icon_content_description)
            )
        }
    }
}