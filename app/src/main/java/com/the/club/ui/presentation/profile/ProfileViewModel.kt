package com.the.club.ui.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.the.club.common.CommonKeys
import com.the.club.common.model.Resource
import com.the.club.domain.model.Profile
import com.the.club.domain.repository.ProfileRepository
import com.the.club.ui.presentation.main.states.ProfileState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    fun updateProfile(newProfile: Profile) {
        viewModelScope.launch {
            val updateFlow = profileRepository.updateProfile(newProfile)
            updateFlow.collect {
                when (it) {
                    is Resource.Loading -> _profileState.value = ProfileState(isLoading = true)
                    is Resource.Success -> _profileState.value = ProfileState(profile = it.data)
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _profileState.value = ProfileState(error = error)
                        if (error == CommonKeys.noNetwork) {
                            _profileState.value = ProfileState(error = error)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}