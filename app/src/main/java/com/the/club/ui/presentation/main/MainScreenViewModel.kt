package com.the.club.ui.presentation.main

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.CommonKeys.ok
import com.the.club.common.CommonKeys.tokenExpired
import com.the.club.common.model.Resource
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.CARD
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.PHONE_NUMBER
import com.the.club.domain.model.BonusCard
import com.the.club.domain.repository.*
import com.the.club.domain.use_case.auth.CheckPhoneUseCase
import com.the.club.domain.use_case.push.PushUseCase
import com.the.club.ui.presentation.main.states.BannersState
import com.the.club.ui.presentation.main.states.BonusCardState
import com.the.club.ui.presentation.main.states.CounterState
import com.the.club.ui.presentation.main.states.ProfileState
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val prefsRepository: PreferenceRepository,
    private val bonusCardsRepository: BonusCardsRepository,
    private val bannersRepository: BannersRepository,
    private val checkPhoneUseCase: CheckPhoneUseCase,
    private val profileRepository: ProfileRepository,
    private val notificationsRepository: NotificationsRepository,
    private val surveysRepository: SurveysRepository,
    private val pushUseCase: PushUseCase
) : ViewModel() {

    private val _cardsState = mutableStateOf(BonusCardState())
    val cardsState: State<BonusCardState> = _cardsState
    var mainCard by mutableStateOf<BonusCard?>(null)

    private val _bannersState = mutableStateOf(BannersState())
    val bannersState: State<BannersState> = _bannersState
    var bannerIndex2Show = mutableStateOf(0)
    private var timer by mutableStateOf<Flow<Int>?>(null)
    private var jobForCancel : Job? = null

    private val _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    private val _notificationState = mutableStateOf(CounterState())
    val notificationState: State<CounterState> = _notificationState

    private val _surveysState = mutableStateOf(CounterState())
    val surveysState: State<CounterState> = _surveysState

    var errorMessage by mutableStateOf("")

    val totalCounter = mutableStateOf(0)
    private val bannersQuantity = mutableStateOf(0)

    fun sendReadPush(pushId: String){
        viewModelScope.launch {
            val body = HashMap<String, String>()
            body["status"] = "viewed"
            val pushFlow = pushUseCase.invoke(pushId, body)
            pushFlow.collect { res->
                when (res) {
                    is Resource.Success -> Log.d("Push", "Push was sent")
                    is Resource.Error -> Log.d("Push", "Push was not sent")
                    else -> Unit
                }
            }
        }
    }

    fun loadCards(isFullScope: Boolean) {
        viewModelScope.launch {
            getCards(isFullScope)
        }
    }

    private suspend fun getCards(isFullScope: Boolean) {
        val cardsFlow = bonusCardsRepository.getBonusCards()
        cardsFlow.collect {
            when (it) {
                is Resource.Loading -> _cardsState.value = BonusCardState(isLoading = true)
                is Resource.Success -> {
                    val aCards = it.data.sortedBy { cards -> cards.status }
                    _cardsState.value = BonusCardState(cards = aCards)
                    val cardInString = Gson().toJson(it.data[0])
                    prefsRepository.setValue(tag = CARD, value = cardInString)
                    if (isFullScope) {
                        getBanners()
                        getProfile()
                        getUnReadNotifications()
                        getAvailableSurveysCount()
                    }
                }
                is Resource.Error -> {
                    val error = it.error?.errorMessage
                        ?: it.exception?.localizedMessage
                        ?: it.message
                    _cardsState.value = BonusCardState(error = error)
                    it.error?.errorCode?.let { code ->
                        if (code == tokenExpired) errorMessage = tokenExpired
                    }
                    if (error == noNetwork) {
                        _bannersState.value = BannersState(error = error)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun getBanners() {
        viewModelScope.launch {
            val bannersFlow = bannersRepository.getBanners()
            bannersFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        _bannersState.value = BannersState(banners = it.data)
                        bannersQuantity.value = it.data.size
                        timer = (0..Int.MAX_VALUE)
                            .asSequence()
                            .asFlow()
                            .onEach { delay(4_000) }

                        counter4banner2show(true)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _bannersState.value = BannersState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            val profileFlow = profileRepository.getProfile()
            profileFlow.collect {
                when (it) {
                    is Resource.Success -> _profileState.value = ProfileState(profile = it.data)
                    else -> _profileState.value = ProfileState(profile = null)
                }
            }
        }
    }

    private fun getUnReadNotifications() {
        viewModelScope.launch {
            val notificationsFlow = notificationsRepository.getUnreadNotifications()
            notificationsFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        _notificationState.value = CounterState(number = it.data.value)
                        totalCounter.value = totalCounter.value +  it.data.value
                    }
                    else -> _notificationState.value = CounterState(number = 0)
                }
            }
        }
    }

    private fun getAvailableSurveysCount() {
        viewModelScope.launch {
            val surveysFlow = surveysRepository.getAvailableSurveysCount()
            surveysFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        _surveysState.value = CounterState(number = it.data.value)
                        totalCounter.value = totalCounter.value +  it.data.value
                    }
                    else -> _surveysState.value = CounterState(number = 0)
                }
            }
        }
    }

    fun checkPhoneNumber() {
        val phone = prefsRepository.getStringValue(PHONE_NUMBER)
        checkPhoneUseCase(phone).onEach { result ->
            when (result) {
                is Resource.Success -> errorMessage = ok
                is Resource.Error -> {
                    errorMessage = result.message.also {
                        if (it == noNetwork) {
                            _cardsState.value = BonusCardState(error = it)
                            _bannersState.value = BannersState(error = it)
                        }
                    }
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun savedBonusCard(): BonusCard = Gson().fromJson(prefsRepository.getStringValue(tag = CARD), BonusCard::class.java)

    fun exitApp(ctx: Context) {
        thread {
            while (true) {
                Thread.sleep(100)
                viewModelScope.launch {
                    deleteAppData(ctx)
                }
            }
        }
    }

    private fun deleteAppData(ctx: Context) {
        try {
            val packageName = ctx.packageName
            val runtime = Runtime.getRuntime()
            runtime.exec("pm clear $packageName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun counter4banner2show(isScrolling: Boolean){
        if (isScrolling) {
            if (timer != null)
                jobForCancel = viewModelScope.launch {
                timer?.takeWhile { isScrolling }?.collect {
                    if (bannerIndex2Show.value == bannersQuantity.value) bannerIndex2Show.value = 0
                    else bannerIndex2Show.value++
                }
            }
        } else {
            bannerIndex2Show.value = 0
            timer = null
            jobForCancel?.cancel()
        }
    }
}