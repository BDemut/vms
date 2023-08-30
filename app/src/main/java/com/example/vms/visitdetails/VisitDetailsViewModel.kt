package com.example.vms.visitdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vms.model.Visit
import com.example.vms.model.repo.VisitRepository
import com.example.vms.user.User
import com.example.vms.userComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by mśmiech on 24.08.2023.
 */
class VisitDetailsViewModel(
    visitId: String,
    signInUser: User,
    private val visitRepository: VisitRepository
) : ViewModel() {
    val state = MutableStateFlow(
        VisitDetailsState(
            true,
            dummyVisit,
            dummyVisit.host == signInUser
        )
    )

    init {
        viewModelScope.launch {
            val visit = visitRepository.getVisit(visitId)
            state.update {
                it.copy(
                    isLoading = false,
                    visit = visit
                )
            }
        }
    }

    fun onDiscardButtonClicked() {
        //TODO
    }

    fun onEditButtonClicked() {
        //TODO
    }

    fun onChangeHostButtonClicked() {
        //TODO
    }

    fun onCancelVisitButtonClicked() {
        //TODO
    }

    class Factory(private val visitId: String) : ViewModelProvider.Factory {
        @Inject
        @Named("signInUser")
        lateinit var signInUser: User

        @Inject
        lateinit var visitRepository: VisitRepository

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userComponent().inject(this)
            return VisitDetailsViewModel(
                visitId,
                signInUser,
                visitRepository
            ) as T
        }
    }
}

val dummyVisit =
    Visit("", "", LocalDateTime.now(), LocalDateTime.now(), null, emptyList(), User("", ""))