package com.the.club.data.remote.surveys.dto

import com.google.gson.annotations.SerializedName
import com.the.club.domain.model.Survey
import java.util.*

data class SurveyDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("state")
    val state: String,
    @SerializedName("survey")
    val surveyDetails: SurveyDetails
)

data class SurveyDetails(
    @SerializedName("description")
    val description: String,
    @SerializedName("dt_start")
    val dateStart: Date,
    @SerializedName("dt_stop")
    val dateStop: Date,
    @SerializedName("title")
    val title: String
)

fun SurveyDto.toSurvey() = Survey(
    id = this.id,
    title = this.surveyDetails.title,
    dateStart = this.surveyDetails.dateStart,
    dateEnd = this.surveyDetails.dateStop,
)