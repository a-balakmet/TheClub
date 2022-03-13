package com.the.club.common.model

import com.google.gson.annotations.SerializedName

data class Pageable<T>(
    @SerializedName("content")
    val content: List<T>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: String?,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

fun <T, E> Pageable<T>.map(mapper: (T) -> E): Pageable<E> =
    Pageable(content = content.map(mapper), page, size, sort, totalElements, totalPages)