package com.the.club.data.remote.surveys

import com.the.club.common.model.Counters
import com.the.club.data.remote.surveys.dto.AnswersDto
import com.the.club.data.remote.surveys.dto.SurveyDto
import com.the.club.data.remote.surveys.dto.SurveyQuestionsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SurveysApi {

    @GET("v3/surveys/count")
    suspend fun getAvailableSurveysCount(): Response<Counters>

    @GET("v3/surveys")
    suspend fun getSurveys(): Response<List<SurveyDto>>

    @GET("v3/surveys/{id}")
    suspend fun getSurveyQuestions(@Path("id") surveyID: Long): Response<SurveyQuestionsDto>

    @POST("v3/surveys/{id}/answers")
    suspend fun sendSurveyAnswers(
        @Path("id") surveyID: Long,
        @Body body: AnswersDto
    ): Response<Unit>
}