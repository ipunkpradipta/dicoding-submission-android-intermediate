package com.ipunkpradipta.submissionstoryapp.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ipunkpradipta.submissionstoryapp.data.PostStoriesRequest
import com.ipunkpradipta.submissionstoryapp.data.Result
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.response.StoryItem

class StoriesViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    fun getStories(token: String):LiveData<PagingData<StoryItem>>{
        return storiesRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun postStories(postStoriesRequest: PostStoriesRequest):LiveData<Result<DefaultResponse>> = storiesRepository.postStories(postStoriesRequest)

}