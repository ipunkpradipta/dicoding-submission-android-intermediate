package com.ipunkpradipta.submissionstoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ipunkpradipta.submissionstoryapp.data.AuthRepository
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