package com.ipunkpradipta.submissionstoryapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.network.*
import com.ipunkpradipta.submissionstoryapp.utils.Event
import com.ipunkpradipta.submissionstoryapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoriesViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getStories(token: String):LiveData<PagingData<StoryItem>>{
        return storiesRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun postStory(token:String,getFile: File,description:String,latitude:String,longitude:String){
        _isLoading.value = true
        val file = reduceFileImage(getFile)

        val description = description.toRequestBody("text/plain".toMediaType())
        val latitude = latitude.toRequestBody("text/plain".toMediaType())
        val longitude = longitude.toRequestBody("text/plain".toMediaType())

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().uploadImage("Bearer $token",imageMultipart,description,latitude,longitude)
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isError.value = responseBody.error
                        _snackbarText.value = Event(responseBody.message)
                    }
                } else {
                    _isError.value = true
                    _snackbarText.value = Event(response.message())
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                _snackbarText.value = Event(t.message + " Please check your internet connection")
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}