package com.ipunkpradipta.submissionstoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiService
import com.ipunkpradipta.submissionstoryapp.network.LoginRequest
import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService, private val authPreferences: AuthPreferences) {

    fun postRegister(registerRequest: RegisterRequest):LiveData<Result<DefaultResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postRegister(registerRequest)
            emit(Result.Success(response))
        }catch (e:Exception){
            Log.d("AuthRepository", "postRegister: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postLogin(loginRequest: LoginRequest) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(loginRequest)
            emit(Result.Success(response))
        }catch (e:Exception){
            Log.d("AuthRepository", "postRegister: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveTokenAuth(token:String){
        authPreferences.saveToken(token)
    }
}