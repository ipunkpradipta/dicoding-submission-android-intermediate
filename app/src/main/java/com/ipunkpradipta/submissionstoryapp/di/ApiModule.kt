package com.ipunkpradipta.submissionstoryapp.di

import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiConfig
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()
}