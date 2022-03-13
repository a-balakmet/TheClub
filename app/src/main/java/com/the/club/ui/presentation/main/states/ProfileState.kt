package com.the.club.ui.presentation.main.states

import com.the.club.data.remote.profile.dto.ProfileDto

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: ProfileDto? = null,
    val error: String = ""
)
