package com.the.club.ui.presentation.surveys.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.the.club.data.remote.surveys.dto.Answer
import com.the.club.data.remote.surveys.dto.Question
import com.the.club.ui.presentation.surveys.SurveyViewModel
import com.the.club.ui.theme.*

@ExperimentalComposeUiApi
@Composable
fun QuestionList(question: Question, viewModel: SurveyViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxSize()) {
        when (question.type) {
            "select" -> {
                if (question.options.isNotEmpty()) {
                    Text(
                        text = question.content,
                        style = Typography.h4,
                        color = textColor(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp)
                    )
                }
            }
            "text" -> {
                Text(
                    text = question.content,
                    style = Typography.h4,
                    color = textColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                )
            }
        }

        when (question.type) {
            "select" -> {
                if (question.options.size > 2) {
                    for (i in 0 until question.options.size) {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (i == question.options.size) 0.dp else 16.dp),
                            option = question.options[i],
                            text = question.options[i].content,
                            isCenter = false,
                            onClick = {
                                for (anOption in question.options) {
                                    anOption.isSelected.value = false
                                }
                                question.options[i].isSelected.value = true
                                val answer = Answer(
                                    question_id = question.options[i].parentId!!,
                                    question_option_id = question.options[i].id,
                                    text_value = question.options[i].content
                                )
                                viewModel.saveAnswer(answer)
                            }
                        )
                    }
                } else if (question.options.size == 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1F),
                            option = question.options[0],
                            text = question.options[0].content,
                            isCenter = true,
                            onClick = {
                                question.options[0].isSelected.value = true
                                question.options[1].isSelected.value = false
                                val answer = Answer(
                                    question_id = question.options[0].parentId!!,
                                    question_option_id = question.options[0].id,
                                    text_value = question.options[0].content
                                )
                                viewModel.saveAnswer(answer)
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedButton(
                            modifier = Modifier.weight(1F),
                            option = question.options[1],
                            text = question.options[1].content,
                            isCenter = true,
                            onClick = {
                                question.options[1].isSelected.value = true
                                question.options[0].isSelected.value = false
                                val answer = Answer(
                                    question_id = question.options[1].parentId!!,
                                    question_option_id = question.options[1].id,
                                    text_value = question.options[1].content
                                )
                                viewModel.saveAnswer(answer)
                            }
                        )
                    }
                }
            }
            "text" -> {
                val inputAnswer = remember { mutableStateOf("") }
                TextField(
                    value = inputAnswer.value,
                    onValueChange = {
                        inputAnswer.value = it
                        val answer = Answer(
                            question_id = question.id,
                            question_option_id = 0,
                            text_value = it
                        )
                        viewModel.saveAnswer(answer)
                    },
                    textStyle = Typography.h4,
                    enabled = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {keyboardController?.hide()}),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .border(width = 1.dp, color = if (inputAnswer.value.isNotEmpty()) pink else grayMedium, shape = RoundedCornerShape(10.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = textColor(),
                        textColor = pink,
                        backgroundColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
            }
        }
    }
}