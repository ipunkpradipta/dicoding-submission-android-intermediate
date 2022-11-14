package com.ipunkpradipta.submissionstoryapp.di

import android.content.Context
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.data.local.room.StoriesDatabase
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiConfig


object Injection {
    fun provideStoriesRepository(context: Context): StoriesRepository {
        val database = StoriesDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(database,apiService)
    }
}