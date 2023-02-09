package com.kotlin.jettrivia.screens

import androidx.compose.runtime.Composable
import com.kotlin.jettrivia.components.Questions


@Composable
fun MyTrivia(viewModel: MainScreenViewModel) {
    Questions(viewModel)
}