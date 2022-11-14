package com.ipunkpradipta.submissionstoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.GsonBuilder
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiService
import com.ipunkpradipta.submissionstoryapp.data.remote.LoginRequest
import com.ipunkpradipta.submissionstoryapp.data.remote.response.LoginResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.RegisterRequest
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class AuthRepository @Inject constructor(private val apiService: ApiService, private val authPreferences: AuthPreferences) {

    fun postRegister(registerRequest: RegisterRequest):LiveData<Result<DefaultResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postRegister(registerRequest)
            emit(Result.Success(response))
            Log.d("AuthRepository", "result: $response ")
        }catch (throwable: Throwable){
            when (throwable) {
                is IOException -> Result.Error("NetworkError")
                is HttpException -> {
                    try {
                        val gson = GsonBuilder().create()
                        try{
                            val response:DefaultResponse = gson.fromJson(
                                throwable.response()?.errorBody()?.string(),
                                DefaultResponse::class.java
                            )
                            if(response.error){
                                emit(Result.Error(response.message))
                            }else{
                                emit(Result.Success(response))
                            }
                            Log.d("AuthRepository", "response: $response} ")
                        }catch (e:IOException){
                            Log.d("AuthRepository", "exceptionGson: ${e.message.toString()} ")
                            emit(Result.Error("gsonException: ${e.message.toString()}"))

                        }
                    } catch (e: JSONException) {
                        emit(Result.Error(e.printStackTrace().toString()))
                        e.printStackTrace()
                    }
                }
                else -> {
                    emit(Result.Error("Call API Network Error"))
                }
            }
        }
    }

    fun postLogin(loginRequest: LoginRequest):LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(loginRequest)
            emit(Result.Success(response))
        }catch (throwable: Throwable){
            when (throwable) {
                is IOException -> Result.Error("NetworkError")
                is HttpException -> {
                    try {
                        val gson = GsonBuilder().create()
                        try{
                            val response:DefaultResponse = gson.fromJson(
                                throwable.response()?.errorBody()?.string(),
                                DefaultResponse::class.java
                            )
                            if(response.error){
                                emit(Result.Error(response.message))
                            }
                            Log.d("AuthRepository", "response: $response} ")
                        }catch (e:IOException){
                            Log.d("AuthRepository", "exceptionGson: ${e.message.toString()} ")
                            emit(Result.Error("gsonException: ${e.message.toString()}"))

                        }
                    } catch (e: JSONException) {
                        emit(Result.Error(e.printStackTrace().toString()))
                        e.printStackTrace()
                    }
                }
                else -> {
                    emit(Result.Error("Call API Network Error"))
                }
            }
        }
    }

    suspend fun saveTokenAuth(token:String){
        authPreferences.saveToken(token)
    }

    fun getTokenAuth():LiveData<String> = authPreferences.getToken().asLiveData()

    suspend fun deleteTokenAuth() = authPreferences.removeToken()



}