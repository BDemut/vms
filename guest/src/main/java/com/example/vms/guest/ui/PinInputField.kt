package com.example.vms.guest.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.vms.guest.home.PIN_LENGTH
import com.example.vms.guest.ui.theme.Purple200


@Composable
fun PinInputField(
    modifier: Modifier = Modifier,
    pinInput: String,
    onPinInputChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(
        modifier = modifier.focusRequester(focusRequester)
            .border(2.dp, MaterialTheme.colors.primary)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        value = TextFieldValue(pinInput, selection = TextRange(pinInput.length)),
        onValueChange = {
            if (it.text.length <= PIN_LENGTH) {
                onPinInputChanged(it.text)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(PIN_LENGTH) { index ->
                    SingleCharacterText(
                        index = index,
                        text = pinInput
                    )
                    if (index == 2) Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun SingleCharacterText(index: Int, text: String) {
    Text(
        modifier = Modifier
            .padding(2.dp)
            .width(32.dp),
        text = when {
            index >= text.length -> "  "
            else -> "${text[index]}"
        },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4.copy(
            textDecoration = TextDecoration.Underline
        ),
    )
}