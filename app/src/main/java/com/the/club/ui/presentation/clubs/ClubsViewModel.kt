package com.the.club.ui.presentation.clubs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.model.clubs.Club
import com.the.club.domain.repository.ClubsRepository
import com.the.club.ui.presentation.clubs.states.ClubsState
import javax.inject.Inject

@HiltViewModel
class ClubsViewModel @Inject constructor(
    private val clubsRepository: ClubsRepository
): ViewModel() {

    private val _clubsState = mutableStateOf(ClubsState())
    val clubsState: State<ClubsState> = _clubsState
    private var theList: ArrayList<Club> = ArrayList()

    init {
        viewModelScope.launch { loadClubs() }
    }

    fun runLoadingClubs(){
        viewModelScope.launch { loadClubs() }
    }

    private suspend fun loadClubs() {
        theList = ArrayList()
        val allClubsFlow = clubsRepository.getAllClubs()
        allClubsFlow.collect {
            when (it) {
                is Resource.Loading -> _clubsState.value = ClubsState(isLoading = true)
                is Resource.Success -> {
                    if (it.data.isNotEmpty()) {
                        it.data.forEach { aClub ->
                            theList.add(aClub)
                        }
                        _clubsState.value = ClubsState(isLoading = true)
                        loadClientClubs()
                    } else {
                        _clubsState.value = ClubsState(isEmptyList = true)
                    }
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _clubsState.value = ClubsState(error = error)

                }
                else -> Unit
            }
        }
        if (theList.isNotEmpty()) {
            _clubsState.value = ClubsState(clubs = theList)
        } else {
            _clubsState.value = ClubsState(isEmptyList = true)
        }
    }

    private suspend fun loadClientClubs() {
        val clientClubsFlow = clubsRepository.getClientClubs()
        clientClubsFlow.collect {
            when (it) {
                is Resource.Loading -> _clubsState.value = ClubsState(isLoading = true)
                is Resource.Success -> {
                    if (it.data.isNotEmpty()) {
                        it.data.forEach { aClub ->
                            theList.map { theClub->
                                if (theClub.id == aClub.id)
                                    theClub.isMember = true
                            }
                        }
                    }
                    _clubsState.value = ClubsState(clubs = theList)
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _clubsState.value = ClubsState(error = error)

                }
                else -> Unit
            }
        }
    }
}