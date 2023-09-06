package com.example.vms.editvisit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vms.R
import com.example.vms.editvisit.model.Room
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.Shapes

/**
 * Created by mśmiech on 01.09.2023.
 */
@Composable
fun SelectRoomView(viewModel: SelectRoomViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            TopBar(onCloseButtonClick = { viewModel.onCloseButtonClicked() })
            if (state.isLoading) {
                LoadingView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                )
            } else {
                RoomList(Modifier.weight(1.0f), state.rooms, { viewModel.onRoomClicked(it) })
            }
        }
    }
}

@Composable
private fun RoomList(modifier: Modifier, rooms: List<Room>, onRoomClick: (Room) -> Unit) {
    LazyColumn(
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        rooms.forEach {
            item {
                RoomItem(
                    room = it,
                    onClick = onRoomClick
                )
            }
        }
    }
}

@Composable
private fun TopBar(onCloseButtonClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.select_room),
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 16.sp
        )
        IconButton(
            onClick = onCloseButtonClick,
            modifier = Modifier.padding(8.dp, 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.close_icon_content_description)
            )
        }
    }
}

@Composable
fun RoomItem(
    room: Room,
    onClick: (Room) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(room) },
        shape = Shapes.medium,
        border = if (room.isSelected) BorderStroke(3.dp, Color(32, 193, 32, 255)) else null
    ) {
        Column {
            Text(
                modifier = Modifier.padding(8.dp),
                text = room.name,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = (8.dp)),
                text = if (room.isAvailable) stringResource(R.string.room_available) else stringResource(
                    R.string.room_unavailable
                )
            )
        }
    }
}