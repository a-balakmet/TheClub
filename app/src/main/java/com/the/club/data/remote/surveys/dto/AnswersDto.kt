package com.the.club.data.remote.surveys.dto

data class AnswersDto(
    val answers: List<Answer>,
    val comment: String
)

data class Answer(
    val question_id: Int,
    val question_option_id: Int,
    val text_value: String
)