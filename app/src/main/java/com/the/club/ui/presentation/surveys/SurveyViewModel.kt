package com.the.club.ui.presentation.surveys

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.data.remote.surveys.dto.Answer
import com.the.club.data.remote.surveys.dto.AnswersDto
import com.the.club.data.remote.surveys.dto.Option
import com.the.club.data.remote.surveys.dto.Question
import com.the.club.domain.repository.SurveysRepository
import com.the.club.ui.presentation.surveys.states.AnswersUploadingState
import com.the.club.ui.presentation.surveys.states.QuestionsState
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val surveysRepository: SurveysRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val surveyId: Long = savedStateHandle["surveyId"] ?: 1
    private val _questionsState = mutableStateOf(QuestionsState())
    val questionsState: State<QuestionsState> = _questionsState

    var expectingAnswersCount = mutableStateOf(0)
    var inputtedAnswersCount = mutableStateOf(0)
    private var answersList: ArrayList<Answer> = ArrayList()

    private val _answersState = mutableStateOf(AnswersUploadingState())
    val answersState: State<AnswersUploadingState> = _answersState

    init {
        getSurvey()
    }

    private fun getSurvey() {
        viewModelScope.launch {
            val questionsFlow = surveysRepository.getSurveyQuestions(surveyId)
            questionsFlow.collect {
                when (it) {
                    is Resource.Loading -> _questionsState.value = QuestionsState(isLoading = true)
                    is Resource.Success -> {
                        val questionsList: ArrayList<Question> = ArrayList()
                        for (aQuestion in it.data.survey.questions) {
                            val optionsList: ArrayList<Option> = ArrayList()
                            for (anOption in aQuestion.options) {
                                val option = Option(
                                    content = anOption.content,
                                    id = anOption.id,
                                    sortOrder = anOption.sortOrder,
                                    targetQuestionId = anOption.targetQuestionId,
                                    isSelected = mutableStateOf(false),
                                    parentId = aQuestion.id
                                )
                                optionsList.add(option)
                            }
                            val question = Question(
                                content = aQuestion.content,
                                id = aQuestion.id,
                                isRoot = aQuestion.isRoot,
                                options = optionsList,
                                sortOrder = aQuestion.sortOrder,
                                type = aQuestion.type
                            )
                            expectingAnswersCount.value++
                            questionsList.add(question)
                        }
                        _questionsState.value = QuestionsState(questions = questionsList)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _questionsState.value = QuestionsState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun saveAnswer(answer: Answer) {
        answersList.apply {
            if (this.isNotEmpty()) {
                this.removeIf { it.question_id == answer.question_id }
            }
            if (answer.text_value.isNotEmpty()) {
                this.add(answer)
            }
        }
        inputtedAnswersCount.value = answersList.size
    }

    fun uploadAnswers(){
        viewModelScope.launch {
            val answers = AnswersDto(answers = answersList, comment = "")
            val answersFlow = surveysRepository.senSurveyAnswers(surveyId = surveyId, answers = answers)
            answersFlow.collect {
                when (it) {
                    is Resource.Loading -> _answersState.value = AnswersUploadingState(isLoading = true)
                    is Resource.Success -> _answersState.value = AnswersUploadingState(result = true)
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _answersState.value = AnswersUploadingState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }
}