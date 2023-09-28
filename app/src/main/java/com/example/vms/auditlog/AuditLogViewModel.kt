package com.example.vms.auditlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vms.user.User
import com.example.vms.userComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by mśmiech on 12.09.2023.
 */
class AuditLogViewModel(
    private val generateAuditLogUseCase: GenerateAuditLogUseCase,
    signInUser: User
) : ViewModel() {
    private val _events: MutableSharedFlow<AuditLogEvent> = MutableSharedFlow()
    val events: SharedFlow<AuditLogEvent> = _events
    val state = MutableStateFlow(
        AuditLogState(
            isProcessing = false,
            startDate = LocalDate.now().minusDays(1),
            startTime = LocalTime.now().withSecond(0).withNano(0),
            endDate = LocalDate.now(),
            endTime = LocalTime.now().withSecond(0).withNano(0),
            isGenerationFailedSnackbarShowing = false,
            isGenerationSucceedDialogShowing = false,
            signInUserEmail = signInUser.email
        )
    )

    fun changeStartDate(startDate: LocalDate) {
        state.update {
            it.copy(
                startDate = startDate,
            )
        }
    }

    fun changeStartTime(startTime: LocalTime) {
        state.update {
            it.copy(
                startTime = startTime,
            )
        }
    }

    fun changeEndDate(endDate: LocalDate) {
        state.update {
            it.copy(
                endDate = endDate,
            )
        }
    }

    fun changeEndTime(endTime: LocalTime) {
        state.update {
            it.copy(
                endTime = endTime,
            )
        }
    }

    fun onGenerateClicked() {
        this@AuditLogViewModel.state.update { it.copy(isProcessing = true) }
        val state = state.value
        val startDateTime = LocalDateTime.of(state.startDate, state.startTime)
        val endDateTime = LocalDateTime.of(state.endDate, state.endTime)
        viewModelScope.launch(Dispatchers.IO) {
            val succeed = generateAuditLogUseCase(startDateTime, endDateTime)
            if (succeed) {
                this@AuditLogViewModel.state.update {
                    it.copy(
                        isGenerationSucceedDialogShowing = true,
                        isProcessing = false
                    )
                }
            } else {
                this@AuditLogViewModel.state.update {
                    it.copy(
                        isGenerationFailedSnackbarShowing = true,
                        isProcessing = false
                    )
                }
            }
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            _events.emit(AuditLogEvent.Finish)
        }
    }

    fun dismissGenerationFailedSnackbar() {
        state.update { it.copy(isGenerationFailedSnackbarShowing = false) }
    }

    fun onGenerationSucceedDialogConfirmed() {
        state.update { it.copy(isGenerationSucceedDialogShowing = false) }
    }

    class Factory : ViewModelProvider.Factory {
        @Inject
        lateinit var generateAuditLogUseCase: GenerateAuditLogUseCase

        @Inject
        @Named("signInUser")
        lateinit var signInUser: User

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userComponent().inject(this)
            return AuditLogViewModel(generateAuditLogUseCase, signInUser) as T
        }
    }
}