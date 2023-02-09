package com.kotlin.jettrivia.di

import com.kotlin.jettrivia.network.QuestionAPI
import com.kotlin.jettrivia.repository.JetRepository
import com.kotlin.jettrivia.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JetTriviaModule {

    @Singleton
    @Provides
    fun getRepository(api: QuestionAPI) = JetRepository(api)

    @Singleton
    @Provides
    fun getRetrofit(): QuestionAPI {
        return Retrofit.Builder()
            .baseUrl(Constant.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionAPI::class.java)
    }
}