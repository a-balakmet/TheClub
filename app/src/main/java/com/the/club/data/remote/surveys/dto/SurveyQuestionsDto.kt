package com.the.club.data.remote.surveys.dto

import androidx.compose.runtime.MutableState
import com.google.gson.annotations.SerializedName
import java.util.*

data class SurveyQuestionsDto(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("state")
    val state: String,
    @SerializedName("survey")
    val survey: Survey
)

data class Survey(
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("dt_created")
    val dateCreated: Date,
    @SerializedName("dt_start")
    val dateStart: Date,
    @SerializedName("dt_stop")
    val dateStop: Date,
    @SerializedName("img_url")
    val imgUrl: String,
    @SerializedName("questions")
    val questions: List<Question>,
    @SerializedName("show_results")
    val showResults: Boolean,
    @SerializedName("title")
    val title: String
)

data class Question(
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_root")
    val isRoot: Boolean,
    @SerializedName("options")
    val options: List<Option>,
    @SerializedName("sort_order")
    val sortOrder: Int,
    @SerializedName("type")
    val type: String
)

data class Option(
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("sort_order")
    val sortOrder: Int,
    @SerializedName("target_question_id")
    val targetQuestionId: Int,
    var isSelected: MutableState<Boolean>,
    var parentId: Int?
)