package com.example.vms.editvisit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

class EditVisitActivity : ComponentActivity() {
    val viewModel: EditVisitViewModel by viewModels()
    lateinit var datePicker: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                EditVisitScreen(viewModel)
            }
        }
    }

    private fun createDatePicker(context: Context, date: LocalDate, onDatePicked: (LocalDate) -> Unit): DatePickerDialog {
        val year = date.year
        val month = date.monthValue - 1
        val dayOfMonth = date.dayOfMonth
        val datePicker = DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                val pickedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                onDatePicked(pickedDate)
            }, year, month, dayOfMonth
        )
        return datePicker
    }

    private fun createTimePicker(context: Context, time: LocalTime, onTimePicked: (LocalTime) -> Unit): TimePickerDialog {
        val hour = time.hour
        val minute = time.minute
        val timePicker = TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                val pickedTime = LocalTime.of(selectedHour, selectedMinute)
                onTimePicked(pickedTime)
            }, hour, minute, true
        )
        return timePicker
    }

    @Composable
    fun EditVisitScreen(viewModel: EditVisitViewModel) {
        val quests = listOf(
            Guest("123", "jan@test.com"),
            Guest("123", "bartek@test.com"),
            Guest("123", "michal@test.com"),
        )
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Topbar()
                Title()
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                )
                DateTimeSection({}, {}, {})
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                )
                LocationSection(null)
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                )
                GuestsSection(quests)
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    @Composable
    fun Topbar() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    //TODO
                },
                modifier = Modifier.padding(8.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_icon_content_description)
                )
            }
            Button(
                onClick = {
                    //TODO
                },
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                Text(
                    text = "Zapisz"
                )
            }
        }
    }

    @Composable
    fun Title() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            NoShapeTextField(
                value = "",
                onValueChange = { },
                placeholder = {
                    Text(text = "Dodaj tytuł")
                },
                singleLine = true,
            )
        }
    }

    @Composable
    fun NoShapeTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        textStyle: TextStyle = LocalTextStyle.current,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions(),
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        minLines: Int = 1,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        colors: TextFieldColors = TextFieldDefaults.textFieldColors()
    ) {
        // If color is not provided via the text style, use content color as a default
        val textColor = textStyle.color.takeOrElse {
            colors.textColor(enabled).value
        }
        val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

        @OptIn(ExperimentalMaterialApi::class)
        (BasicTextField(
            value = value,
            modifier = modifier
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth,
                    minHeight = TextFieldDefaults.MinHeight
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError).value),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors
                )
            }
        ))
    }

    @Composable
    fun DateTimeSection(
        onDateChanged: (LocalDate) -> Unit,
        onStartTimeChanged: (LocalTime) -> Unit,
        onEndTimeChanged: (LocalTime) -> Unit,
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .padding(13.dp),
                imageVector = Icons.Default.AccessTime,
                contentDescription = stringResource(R.string.datetime_icon_content_description)
            )
            val datePicker = createDatePicker(LocalContext.current, LocalDate.now()) {
                onDateChanged(it)
            }
            TextButton(onClick = { datePicker.show() }) {
                Text(text = "Sob., 18 mar 2023")
            }
        }
        val startTimePicker = createTimePicker(LocalContext.current, LocalTime.now()) {
            onStartTimeChanged(it)
        }
        TextButton(
            onClick = { startTimePicker.show() },
            modifier = Modifier.padding(start = 50.dp)
        ) {
            Text(
                text = "17:00"
            )
        }
        val endTimePicker = createTimePicker(LocalContext.current, LocalTime.now()) {
            onStartTimeChanged(it)
        }
        TextButton(
            onClick = { endTimePicker.show() },
            modifier = Modifier.padding(start = 50.dp)
        ) {
            Text(
                text = "18:00"
            )
        }
    }

    @Composable
    fun LocationSection(room: Room?) {
        val roomLabel = room?.name ?: "Sala konferencyjna"
        Row {
            Icon(
                modifier = Modifier
                    .padding(13.dp),
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.location_icon_content_description)
            )
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = roomLabel)
            }
        }
    }

    @Composable
    fun GuestsSection(guests: List<Guest>) {
        Row {
            Icon(
                modifier = Modifier
                    .padding(13.dp),
                imageVector = Icons.Default.People,
                contentDescription = stringResource(R.string.guests_icon_content_description)
            )
            Column {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Dodaj osoby")
                }
                guests.forEach {
                    Guest(guest = it)
                }
            }
        }
    }

    @Composable
    fun Guest(guest: Guest) {
        Text(text = guest.email,
            modifier = Modifier.padding(8.dp, 8.dp))
    }
}