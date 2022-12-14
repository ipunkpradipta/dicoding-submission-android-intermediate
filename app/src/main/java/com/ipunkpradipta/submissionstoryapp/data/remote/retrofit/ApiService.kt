package com.ipunkpradipta.submissionstoryapp.data.remote.retrofit

import com.ipunkpradipta.submissionstoryapp.data.remote.LoginRequest
import com.ipunkpradipta.submissionstoryapp.data.remote.RegisterRequest
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.response.LoginResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun postRegister(@Body body: RegisterRequest): DefaultResponse

    @POST("login")
    suspend fun postLogin(@Body body: LoginRequest): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): Response<StoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file : MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): DefaultResponse
}