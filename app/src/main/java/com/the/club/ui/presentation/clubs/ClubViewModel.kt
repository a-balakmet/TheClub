package com.the.club.ui.presentation.clubs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.model.clubs.Club
import com.the.club.domain.model.clubs.MembershipType
import com.the.club.domain.repository.ClubsRepository
import com.the.club.domain.use_case.clubs.ClubsParticipationUseCase
import com.the.club.ui.presentation.clubs.states.ClubState
import javax.inject.Inject

@HiltViewModel
class ClubViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val clubsRepository: ClubsRepository,
    private val clubsParticipationUseCase: ClubsParticipationUseCase
) : ViewModel() {

    private val clubId: Int = savedStateHandle["clubId"] ?: 1
    private val _clubState = mutableStateOf(ClubState())
    val clubsState: State<ClubState> = _clubState
    private var theList: ArrayList<Club> = ArrayList()

    val exchangeState = mutableStateOf(false)

    init {
        viewModelScope.launch { loadClubs() }
    }

    private suspend fun loadClubs() {
        val allClubsFlow = clubsRepository.getAllClubs()
        allClubsFlow.collect {
            when (it) {
                is Resource.Loading -> _clubState.value = ClubState(isLoading = true)
                is Resource.Success -> {
                    if (it.data.isNotEmpty()) {
                        it.data.forEach { aClub ->
                            if (aClub.id == clubId) {
                                theList.add(aClub)
                            }
                        }
                        _clubState.value = ClubState(isLoading = true)
                        loadClientClubs()
                    } else {
                        _clubState.value = ClubState(club = null)
                    }
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _clubState.value = ClubState(error = error)

                }
                else -> Unit
            }
        }
    }

    private suspend fun loadClientClubs() {
        val clientClubsFlow = clubsRepository.getClientClubs()
        clientClubsFlow.collect {
            when (it) {
                is Resource.Loading -> _clubState.value = ClubState(isLoading = true)
                is Resource.Success -> {
                    if (it.data.isNotEmpty()) {
                        it.data.forEach { aClub ->
                            theList.map { theClub->
                                if (theClub.id == aClub.id)
                                    theClub.isMember = true
                            }
                        }
                    }
                    _clubState.value = ClubState(club = theList[0])

                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _clubState.value = ClubState(error = error)

                }
                else -> Unit
            }
        }
    }

    fun enterClub() {
        val membershipType = MembershipType(source = "mobile")
        clubsParticipationUseCase.invoke(clubId = theList[0].id, membershipType = membershipType).onEach {
            when (it) {
                is Resource.Loading -> exchangeState.value = true
                is Resource.Success -> {
                    _clubState.value.club?.let { theClub->
                        theClub.isMember = true
                    }
                    exchangeState.value = false
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _clubState.value = ClubState(club = null)
                    _clubState.value = ClubState(error = error)
                    exchangeState.value = false
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun exitClub() {
        clubsParticipationUseCase.invoke(clubId = theList[0].id).onEach {
            when (it) {
                is Resource.Loading -> exchangeState.value = true
                is Resource.Success -> {
                    _clubState.value.club?.let { theClub->
                        theClub.isMember = false
                    }
                    exchangeState.value = false
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _clubState.value = ClubState(club = null)
                    _clubState.value = ClubState(error = error)
                    exchangeState.value = false
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

}