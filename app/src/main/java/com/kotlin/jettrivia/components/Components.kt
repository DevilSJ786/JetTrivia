package com.kotlin.jettrivia.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.jettrivia.model.QuestionItem
import com.kotlin.jettrivia.screens.MainScreenViewModel
import com.kotlin.jettrivia.utils.AppColors

@Composable
fun Questions(viewModel: MainScreenViewModel) {

    val question = viewModel.data.value.data?.toMutableList()
    val questionState = remember {
        mutableStateOf(0)
    }
    if (viewModel.data.value.loading == true) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(AppColors.mDarkPurple)
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (question != null) {
            QuestionDisplay(
                question = question[questionState.value], questionState, question.size
            ) {
                if (question.size > questionState.value) questionState.value += 1
            }
        }
    }
}


@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionState: MutableState<Int>,
    questionCount: Int,
    onNextClick: (Int) -> Unit
) {
    val choiceState = remember(question) {
        question.choices.toMutableStateList()
    }
    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswerState: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choiceState[it] == question.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            ShowProgress(count = questionState.value)
            QuestionTracker(count = questionState.value, outOf = questionCount)
            DrawDottedLine(pathEffect = pathEffect)
            Column {
                Text(
                    text = question.question,
                    color = AppColors.mOffWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxHeight(0.3f)
                        .padding(7.dp)
                )
                choiceState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple, AppColors.mOffDarkPurple
                                    )
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = { updateAnswerState(index) },
                            modifier = Modifier.padding(start = 14.dp),
                            colors = RadioButtonDefaults.colors(
                                if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false && index == answerState.value) {
                                        Color.Red
                                    } else {
                                        AppColors.mOffWhite
                                    },
                                    fontSize = 16.sp
                                )
                            ) { append(answerText) }
                        }
                        Text(text = annotatedString)

                    }
                }
                Button(
                    onClick = {
                        onNextClick(questionState.value)
                    },
                    colors = buttonColors(AppColors.mLightBlue),
                    modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp

                    )
                }
            }
        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(vertical = 7.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(x = 0f, y = 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )

    }
}

@Preview
@Composable
fun ShowProgress(count: Int = 10) {
    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFFF95075), Color(0xFFBE6BE5)
        )
    )
    val progressFactor = remember(count) {
        mutableStateOf(count * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp, brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple, AppColors.mLightPurple
                    )
                ), shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Color.Transparent), verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(1.dp),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(brush = gradient)
        ) {

        }
    }
}

@Composable
fun QuestionTracker(count: Int = 2, outOf: Int = 10) {

    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray, fontWeight = FontWeight.Bold, fontSize = 27.sp
                )
            ) {
                append(text = "Question $count/")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                ) {
                    append(text = "$outOf")
                }
            }
        }
    })

}
