package com.the.club.ui.presentation.surveys.states

import com.the.club.data.remote.surveys.dto.Question

data class QuestionsState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
