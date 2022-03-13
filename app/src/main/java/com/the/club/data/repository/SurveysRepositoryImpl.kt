package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys
import com.the.club.common.model.Counters
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.surveys.SurveysApi
import com.the.club.data.remote.surveys.dto.AnswersDto
import com.the.club.data.remote.surveys.dto.SurveyDto
import com.the.club.data.remote.surveys.dto.SurveyQuestionsDto
import com.the.club.data.remote.surveys.dto.toSurvey
import com.the.club.domain.model.Survey
import com.the.club.domain.repository.SurveysRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SurveysRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val surveysApi: SurveysApi
) : SurveysRepository, NetworkCallResponseAdapter {

    override suspend fun getAvailableSurveysCount(): Flow<Resource<Counters>> = flow {
        emit(Resource.Loading)
        try {
            val response = surveysApi.getAvailableSurveysCount()
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = "no network"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "no network"))
        }
    }

    override suspend fun getAvailableSurveys(): Flow<Resource<List<Survey>>> = flow {
        emit(Resource.Loading)
        try {
            val response = surveysApi.getSurveys()
            val resource = mapResponse(response) {it.map (SurveyDto::toSurvey)}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = "no network"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "no network"))
        }
    }

    override suspend fun getSurveyQuestions(surveyId: Long): Flow<Resource<SurveyQuestionsDto>> = flow {
        emit(Resource.Loading)
        try {
            val response = surveysApi.getSurveyQuestions(surveyId)
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = "no network"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "no network"))
        }
    }

    override suspend fun senSurveyAnswers(surveyId: Long, answers: AnswersDto): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = surveysApi.sendSurveyAnswers(surveyId, answers)
            val resource = mapResponse(response) {}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }

}