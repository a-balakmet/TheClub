package com.the.club.common.model

import com.the.club.domain.model.ErrorDetails

sealed class Resource<out T> {
    class Success<out T>(val data: T) : Resource<T>()
    class Error(
        val message: String = "",
        val exception: Exception? = null,
        val error: ErrorDetails? = null
    ) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object LoadingMore : Resource<Nothing>()
}
