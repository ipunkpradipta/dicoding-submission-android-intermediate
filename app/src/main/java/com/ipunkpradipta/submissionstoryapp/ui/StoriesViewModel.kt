package com.ipunkpradipta.submissionstoryapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ipunkpradipta.submissionstoryapp.data.PostStoriesRequest
import com.ipunkpradipta.submissionstoryapp.data.Result
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiConfig
import com.ipunkpradipta.submissionstoryapp.network.*
import com.ipunkpradipta.submissionstoryapp.utils.Event
import com.ipunkpradipta.submissionstoryapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoriesViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    fun getStories(token: String):LiveData<PagingData<StoryItem>>{
        return storiesRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun postStories(postStoriesRequest: PostStoriesRequest):LiveData<Result<DefaultResponse>> = storiesRepository.postStories(postStoriesRequest)

}