package com.the.club.ui.presentation.main.states

data class CounterState(
    val isLoading: Boolean = false,
    var number: Int = 0,
    val error: String = ""
)
