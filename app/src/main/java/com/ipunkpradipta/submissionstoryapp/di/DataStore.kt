package com.ipunkpradipta.submissionstoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ipunkpradipta.submissionstoryapp.data.AuthPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

@Module
@InstallIn(SingletonComponent::class)
class DataStore {
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore
    fun provideAuthPreferences(dataStore: DataStore<Preferences>):AuthPreferences = AuthPreferences(dataStore)
}