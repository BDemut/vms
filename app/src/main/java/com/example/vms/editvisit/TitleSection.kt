package com.example.vms.editvisit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.example.vms.R

/**
 * Created by mśmiech on 24.08.2023.
 */

@Composable
fun TitleSection(
    title: String,
    onTitleChange: (String) -> Unit,
    isTitleValid: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .let {
                if (!isTitleValid) {
                    it.background(MaterialTheme.colors.error.copy(alpha = 0.5f))
                } else {
                    it
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!isTitleValid) {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(R.string.title_error_icon_content_description),
                tint = MaterialTheme.colors.error
            )
        } else {
            Spacer(modifier = Modifier.width(50.dp))
        }
        val fontSize = 24.sp
        NoShapeTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.edit_visit_title_placeholder),
                    fontSize = fontSize
                )
            },
            singleLine = true,
            textStyle = TextStyle.Default.copy(fontSize = fontSize),
            modifier = Modifier.padding(start = 10.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTitle() {
    TitleSection(title = "", onTitleChange = {}, false)
}