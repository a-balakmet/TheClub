package com.the.club.ui.presentation.surveys.states

data class AnswersUploadingState(
    val isLoading: Boolean = false,
    val result: Boolean = false,
    val error: String = ""
)
