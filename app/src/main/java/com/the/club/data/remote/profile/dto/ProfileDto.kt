package com.the.club.data.remote.profile.dto

import com.google.gson.annotations.SerializedName
import com.the.club.common.model.CityDto
import java.util.*

data class ProfileDto(
    @SerializedName("birthday")
    val birthday: Date?,
    @SerializedName("city")
    val city: CityDto?,
    @SerializedName("dt_created")
    val dateCreated: Date,
    @SerializedName("email")
    var email: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("gender")
    var gender: String?,
    @SerializedName("id")
    val id: Long,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("patronymic")
    val patronymic: String?,
    @SerializedName("phone")
    var phone: String
)