package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.common.model.Counters
import com.the.club.data.remote.surveys.dto.AnswersDto
import com.the.club.data.remote.surveys.dto.SurveyQuestionsDto
import com.the.club.domain.model.Survey

interface SurveysRepository {

    suspend fun getAvailableSurveysCount(): Flow<Resource<Counters>>
    suspend fun getAvailableSurveys(): Flow<Resource<List<Survey>>>
    suspend fun getSurveyQuestions(surveyId: Long): Flow<Resource<SurveyQuestionsDto>>
    suspend fun senSurveyAnswers(surveyId: Long, answers: AnswersDto): Flow<Resource<Unit>>
}