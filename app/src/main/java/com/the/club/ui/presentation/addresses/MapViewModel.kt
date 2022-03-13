package com.the.club.ui.presentation.addresses

import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.the.club.common.LocationRepository
import com.the.club.common.ktx.toLocation
import com.the.club.common.model.Resource
import com.the.club.domain.model.City
import com.the.club.domain.model.shops.Shop
import com.the.club.domain.repository.ShopsRepository
import com.the.club.ui.presentation.addresses.states.AddressesState
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val shopsRepository: ShopsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var hasPermission by mutableStateOf(false)
    private val cityId: Int = savedStateHandle["cityId"] ?: 1

    private val _mapContentState = mutableStateOf(AddressesState())
    val mapContentState: State<AddressesState> = _mapContentState

    var deviceLocation = mutableStateOf<LatLng?>(null)
    val initialCity = mutableStateOf(LatLng(43.206191, 76.897995))
    private val tempCity = initialCity.value.toLocation()
    var onMapLocation = MutableStateFlow(tempCity)
    var nearestCity = mutableStateOf(City(initialCity.value, 1, "Алматы"))
    var cities by mutableStateOf(listOf<City>())
    var shops by mutableStateOf(listOf<Shop>())

    init {
        cities = cities.toMutableList().also { it.add(0, nearestCity.value)}
    }

    fun canGetLocation() = locationRepository.isLocationEnabled()

    fun loadCities(){
        viewModelScope.launch {
            val location = locationRepository.getCurrentLocation()
            if (location != null) {
                deviceLocation.value = LatLng(location.latitude, location.longitude)
                onMapLocation.value = location
                initialCity.value = LatLng(location.latitude, location.longitude)
            } else {
                deviceLocation.value = initialCity.value
            }
            val citiesFlow = shopsRepository.getCities()
            citiesFlow.collect {
                when (it) {
                    is Resource.Loading -> _mapContentState.value = AddressesState(isLoading = true)
                    is Resource.Success -> {
                        if (it.data.isNotEmpty()) {
                            cities = it.data
                            if (location != null) {
                                getNearestCity(location, it.data)
                            } else {
                                nearestCity.value = it.data.find { aCity->
                                    aCity.id == cityId
                                }!!
                            }
                            loadShops()
                        } else {
                            _mapContentState.value = AddressesState(isFinished = false)
                        }
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _mapContentState.value = AddressesState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun loadShops(){
        viewModelScope.launch {
            val shopsFlow = shopsRepository.getShops()
            shopsFlow.collect {
                when (it) {
                    is Resource.Loading -> _mapContentState.value = AddressesState(isLoading = true)
                    is Resource.Success -> {
                        if (it.data.isNotEmpty()) {
                            shops = it.data
                            _mapContentState.value = AddressesState(isFinished = true)
                        } else {
                            _mapContentState.value = AddressesState(isFinished = false)
                        }
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _mapContentState.value = AddressesState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getNearestCity(location: Location, cities: List<City>) {
        var smallestDistance = -1F
        for(city in cities){
            city.geo?.let { cityGeo->
                val cityLocation = Location("test")
                cityLocation.latitude = cityGeo.latitude
                cityLocation.longitude = cityGeo.longitude
                val distance = cityLocation.distanceTo(location)
                if(smallestDistance == -1F || distance < smallestDistance) {
                    nearestCity.value = city
                    smallestDistance = distance
                }
            }
        }
    }
}