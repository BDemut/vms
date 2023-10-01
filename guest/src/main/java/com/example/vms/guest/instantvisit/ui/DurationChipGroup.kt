package com.example.vms.guest.instantvisit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vms.guest.instantvisit.Duration

@Composable
fun DurationChipGroup(
    selectedDuration: Duration,
    onDurationSelected: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Duration.values().forEach {
            Chip(
                name = "${it.value}min",
                isSelected = selectedDuration == it,
                onDurationSelected = { onDurationSelected(it) },
            )
        }
    }
}

@Composable
private fun Chip(
    name: String,
    isSelected: Boolean = false,
    onDurationSelected: () -> Unit = {},
) {
    Surface(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colors.primary)
        } else {
            BorderStroke(1.dp, Color.Gray)
        }
    ) {
        Box(modifier = Modifier
            .defaultMinSize(minWidth = 80.dp)
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onDurationSelected()
                }
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center)
                    .padding(8.dp),
                text = name,
            )
        }
    }
}