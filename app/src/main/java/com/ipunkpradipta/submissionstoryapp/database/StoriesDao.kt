package com.ipunkpradipta.submissionstoryapp.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ipunkpradipta.submissionstoryapp.network.StoryItem

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(quote: List<StoryItem>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): PagingSource<Int, StoryItem>

    @Query("SELECT * FROM stories")
    fun getAllStoriesForMaps(): LiveData<List<StoryItem>>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}