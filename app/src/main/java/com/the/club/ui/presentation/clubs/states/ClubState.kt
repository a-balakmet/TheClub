package com.the.club.ui.presentation.clubs.states

import com.the.club.domain.model.clubs.Club

class ClubState (
    val isLoading: Boolean = false,
    val club: Club? = null,
    val error: String = ""
)