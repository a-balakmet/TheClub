package com.the.club.ui.presentation.clubs.states

import com.the.club.domain.model.clubs.Club

class ClubsState (
    val isLoading: Boolean = false,
    val clubs: List<Club> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)