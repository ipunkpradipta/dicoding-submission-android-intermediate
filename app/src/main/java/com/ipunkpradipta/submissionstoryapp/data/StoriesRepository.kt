package com.ipunkpradipta.submissionstoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ipunkpradipta.submissionstoryapp.data.local.room.StoriesDatabase
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiService
import com.ipunkpradipta.submissionstoryapp.network.StoryItem

class StoriesRepository(private val storiesDatabase: StoriesDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token:String):LiveData<PagingData<StoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService,token),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStories()
            }
        ).liveData
    }

    fun getStoriesForMaps():LiveData<List<StoryItem>> = storiesDatabase.storiesDao().getAllStoriesForMaps()
}