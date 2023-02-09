package com.kotlin.jettrivia.repository

import com.kotlin.jettrivia.data.DataOrException
import com.kotlin.jettrivia.model.Question
import com.kotlin.jettrivia.network.QuestionAPI
import javax.inject.Inject

class JetRepository @Inject constructor(private val api: QuestionAPI) {

    private val dataOrException = DataOrException<Question, Boolean, Exception>()

    suspend fun getAllQuestion(): DataOrException<Question, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

}