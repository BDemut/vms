package com.example.vms.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.home.HomeActivity
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
                is LoginEvent.ShowLoginError -> showLoginErrorToast(event.messageResId)
            }
        }.launchIn(lifecycleScope)
    }

    private fun showLoginErrorToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.onStart()
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
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Username(viewModel, state.displayValidErrors)
            Spacer(modifier = Modifier.height(8.dp))
            Password(viewModel, state.displayValidErrors)
            TextButton(onClick = viewModel::onLoginButtonClicked) {
                Text(stringResource(R.string.login))
            }
        }
        if(state.isLoading) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color(0, 0, 0, 120)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LoadingIndicator()
                }
            }
        }
    }
}

@Composable
fun Username(viewModel: LoginViewModel, displayValidErrors: Boolean) {
    val username = viewModel.username.collectAsState().value
    val isUsernameValid = viewModel.isUsernameValid.collectAsState().value
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = username,
        onValueChange = viewModel::setUsername,
        label = { Text(text = stringResource(id = R.string.username_label)) },
        placeholder = { Text(text = stringResource(id = R.string.username_placeholder)) },
        isError = displayValidErrors && !isUsernameValid,
    )
    if (displayValidErrors && !isUsernameValid) {
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
fun Password(viewModel: LoginViewModel, displayValidErrors: Boolean) {
    val password = viewModel.password.collectAsState().value
    val isPasswordValid = viewModel.isPasswordValid.collectAsState().value
    var showPassword by remember { mutableStateOf(value = false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = viewModel::setPassword,
        label = { Text(text = stringResource(id = R.string.password_label)) },
        placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
        isError = displayValidErrors && !isPasswordValid,
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
                        contentDescription = stringResource(id = R.string.hide_password_icon_content_description)
                    )
                }
            } else {
                IconButton(
                    onClick = { showPassword = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.hide_password_icon_content_description)
                    )
                }
            }
        }
    )
    if (displayValidErrors && !isPasswordValid) {
        TextFieldError(stringResource(id = R.string.password_invalid))
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
    )
}