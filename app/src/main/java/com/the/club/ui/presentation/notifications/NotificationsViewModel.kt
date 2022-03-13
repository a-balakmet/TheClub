package com.the.club.ui.presentation.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.repository.NotificationsRepository
import com.the.club.ui.presentation.notifications.states.NotificationsState
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _notificationsState = mutableStateOf(NotificationsState())
    val notificationsState: State<NotificationsState> = _notificationsState

    init {
        viewModelScope.launch { loadNotifications() }
    }

    private suspend fun loadNotifications() {
        val notificationsFlow = notificationsRepository.getNotifications()
        notificationsFlow.collect {
            when (it) {
                is Resource.Loading -> _notificationsState.value = NotificationsState(isLoading = true)
                is Resource.Success -> {
                    if (it.data.isNotEmpty()) {
                        _notificationsState.value = NotificationsState(notifications = it.data.sortedBy { notification -> notification.date })
                    } else {
                        _notificationsState.value = NotificationsState(isEmptyList = true)
                    }
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _notificationsState.value = NotificationsState(error = error)

                }
                else -> Unit
            }
        }
    }
}