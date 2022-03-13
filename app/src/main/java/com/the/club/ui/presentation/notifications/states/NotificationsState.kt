package com.the.club.ui.presentation.notifications.states

import com.the.club.domain.model.Notification

data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
