package com.the.club.ui.presentation.surveys.states

import com.the.club.domain.model.Survey

data class SurveysState(
    val isLoading: Boolean = false,
    val surveys: List<Survey> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
