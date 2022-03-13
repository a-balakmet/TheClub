package com.the.club.ui.presentation.priceCheck

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.CommonKeys.noProduct
import com.the.club.common.CommonKeys.noShop
import com.the.club.common.LocationRepository
import com.the.club.common.model.Geo
import com.the.club.common.model.Resource
import com.the.club.domain.model.shops.NearestShopsRequest
import com.the.club.domain.model.shops.ProductPriceRequest
import com.the.club.domain.repository.ShopsRepository
import com.the.club.ui.presentation.priceCheck.states.NearestShopState
import com.the.club.ui.presentation.priceCheck.states.NearestShopsState
import com.the.club.ui.presentation.priceCheck.states.ProductsPriceState
import javax.inject.Inject

@HiltViewModel
class PriceCheckViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val shopsRepository: ShopsRepository
) : ViewModel() {

    private val _shopState = mutableStateOf(NearestShopState())
    val shopState: State<NearestShopState> = _shopState

    private val _shopsState = mutableStateOf(NearestShopsState())
    val shopsState: State<NearestShopsState> = _shopsState

    private val _priceState = mutableStateOf(ProductsPriceState())
    val priceState: State<ProductsPriceState> = _priceState

    var isLocationDetected by mutableStateOf(true)

    fun canGetLocation() = locationRepository.isLocationEnabled()

    fun getDeviceLocation() {
        viewModelScope.launch {
            val location = locationRepository.getCurrentLocation()
            if (location != null) {
                val geo = Geo(location.latitude, location.longitude)
                val shopFlow = shopsRepository.getNearestShop(geo)
                shopFlow.collect {
                    when (it) {
                        is Resource.Loading -> _shopState.value = NearestShopState(isLoading = true)
                        is Resource.Success -> _shopState.value = NearestShopState(shop = it.data)
                        is Resource.Error -> {
                            val error = when (it.error?.errorCode) {
                                "actor: record not found" -> noShop
                                else -> it.error?.errorMessage
                                    ?: it.exception?.localizedMessage
                                    ?: it.message
                            }
                            _shopState.value = NearestShopState(error = error)
                        }
                        else -> Unit
                    }
                }
            } else {
                isLocationDetected = false
            }
        }
    }

    fun getNearestShops() {
        viewModelScope.launch {
            locationRepository.getCurrentLocation()?.let { location->
                val request = NearestShopsRequest(k = 3, latitude = location.latitude, longitude = location.longitude)
                val shopsFlow = shopsRepository.getNearestShops(request)
                shopsFlow.collect {
                    when (it) {
                        is Resource.Loading -> _shopsState.value = NearestShopsState(isLoading = true)
                        is Resource.Success -> _shopsState.value = NearestShopsState(shops = it.data)
                        is Resource.Error -> {
                            val error = it.error?.errorMessage
                                ?: it.exception?.localizedMessage
                                ?: it.message
                            _shopsState.value = NearestShopsState(error = error)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    fun getShopByQr(qr: String) {
        viewModelScope.launch {
            val shopFlow = shopsRepository.getShopByQr(qr)
            shopFlow.collect {
                when (it) {
                    is Resource.Loading -> _shopState.value = NearestShopState(isLoading = true)
                    is Resource.Success -> _shopState.value = NearestShopState(shop = it.data)
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _shopState.value = NearestShopState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun getProductPrice(theShopId: Int, barcode: String) {
        viewModelScope.launch {
            val request = ProductPriceRequest(actor_id = theShopId, barcode = barcode)
            val priceFlow = shopsRepository.getProductPrice(request)
            priceFlow.collect {
                when (it) {
                    is Resource.Loading -> _priceState.value = ProductsPriceState(isLoading = true)
                    is Resource.Success -> _priceState.value = ProductsPriceState(productPrice = it.data)
                    is Resource.Error -> {
                        val error = when (it.error?.errorCode) {
                            "ware not found" -> noProduct
                            else -> it.error?.errorMessage
                                ?: it.exception?.localizedMessage
                                ?: it.message
                        }
                        _priceState.value = ProductsPriceState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun dropShopValue() {
        _shopState.value = NearestShopState(shop = null)
    }

    fun dropShopsValue() {
        _shopsState.value = NearestShopsState(shops = emptyList())
    }

    fun dropPriceValue() {
        _priceState.value = ProductsPriceState(productPrice = null)
    }

    fun dropErrors() {
        _priceState.value = ProductsPriceState(error = "")
    }
}