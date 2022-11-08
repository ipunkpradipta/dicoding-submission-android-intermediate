package com.ipunkpradipta.submissionstoryapp.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

data class PostStoriesRequest(
    val token:String,
    val desc:RequestBody,
    val file:MultipartBody.Part,
    val lat:RequestBody,
    val lon:RequestBody
)
