package com.kotlin.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.jettrivia.data.DataOrException
import com.kotlin.jettrivia.model.Question
import com.kotlin.jettrivia.repository.JetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val repository: JetRepository) : ViewModel() {

    val data: MutableState<DataOrException<Question,
            Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    init {
        getAllQuestion()
    }

    fun retry() = getAllQuestion()
    private fun getAllQuestion() {
        viewModelScope.launch {
            data.value.loading = true
            val list = async {  data.value = repository.getAllQuestion() }
            list.await()
            if (data.value.data.toString().isNotEmpty()) data.value.loading = false
        }
    }

}