package com.example.vms.editvisit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.editvisit.model.Room
import com.example.vms.editvisit.model.Visit
import com.example.vms.repository.VisitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * Created by mśmiech on 01.09.2023.
 */
class SelectRoomViewModel(
    private val onClose: (Visit.Room?) -> Unit,
    private val visitRepository: VisitRepository
) : ViewModel() {
    val state = MutableStateFlow(
        SelectRoomState(
            rooms = emptyList(),
            isLoading = true
        )
    )
    private var initRoomId: String? = null
    private var selectedRoom: Room? = null

    fun setup(room: Visit.Room?, startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        state.update {
            it.copy(
                rooms = emptyList(),
                isLoading = true
            )
        }
        initRoomId = room?.id
        setupRooms(startDateTime, endDateTime)
    }

    private fun setupRooms(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            val rooms = visitRepository.getRooms(startDateTime, endDateTime)
                .map {
                    Room(
                        id = it.id,
                        name = it.name,
                        isAvailable = it.isAvailable,
                        isSelected = it.id == initRoomId
                    ).also {
                        if (it.isSelected) {
                            selectedRoom = it
                        }
                    }
                }
            state.update {
                it.copy(
                    rooms = rooms,
                    isLoading = false
                )
            }
        }
    }

    fun onCloseButtonClicked() {
        onClose(selectedRoom?.asVisitRoom())
    }

    fun onRoomClicked(room: Room) {
        val rooms = state.value.rooms.toMutableList()
        if (selectedRoom != null) {
            rooms[rooms.indexOf(selectedRoom)] = selectedRoom!!.copy(isSelected = false)
        }
        selectedRoom = if (selectedRoom == room) null else room
        if (selectedRoom != null) {
            val room1 = selectedRoom!!.copy(isSelected = true)
            rooms[rooms.indexOf(selectedRoom)] = room1
            selectedRoom = room1
        }
        state.update { it.copy(rooms = rooms) }
    }

    private fun Room.asVisitRoom() = Visit.Room(this.id, this.name)
}