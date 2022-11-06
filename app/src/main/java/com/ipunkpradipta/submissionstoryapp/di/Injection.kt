package com.ipunkpradipta.submissionstoryapp.di

import android.content.Context
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.database.StoriesDatabase
import com.ipunkpradipta.submissionstoryapp.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val database =StoriesDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(database,apiService)
    }
}