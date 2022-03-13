package com.the.club.data

import com.google.gson.Gson
import com.the.club.common.model.Resource
import com.the.club.domain.model.ErrorDetails
import retrofit2.Response
import java.text.ParseException

interface NetworkCallResponseAdapter {

    val gson: Gson

    fun <T, R> mapResponse(response: Response<T>, mapper: (T) -> R): Resource<R> {
        return if (response.isSuccessful)
            response.body()?.let { body -> Resource.Success(mapper.invoke(body)) }
                ?: Resource.Error(exception = IllegalStateException("Empty body exception"))
        else tryParseError(response)
    }

    fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful)
            response.body()?.let { body -> Resource.Success(body) }
                ?: Resource.Error(exception = IllegalStateException("Empty body exception"))
        else tryParseError(response)
    }

    fun <T> tryParseError(response: Response<T>): Resource.Error {
        val errorBody = response.errorBody()
        return if (errorBody != null) parseErrorBody(errorBody.string())
        else Resource.Error(exception = IllegalStateException("Empty body exception"))
    }

    fun parseErrorBody(errorBody: String): Resource.Error {
        return if (errorBody.contains("error_code")) {
            try {
                val error = gson.fromJson(errorBody, ErrorDetails::class.java)
                Resource.Error(error = error)
            } catch (exception: ParseException) {
                Resource.Error(exception = IllegalStateException("Empty body exception"))
            }
        } else Resource.Error(exception = IllegalStateException("Empty body exception"))
    }
}