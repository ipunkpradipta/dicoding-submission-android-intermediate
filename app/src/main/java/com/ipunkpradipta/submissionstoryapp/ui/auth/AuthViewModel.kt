package com.ipunkpradipta.submissionstoryapp.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipunkpradipta.submissionstoryapp.data.AuthRepository
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiConfig
import com.ipunkpradipta.submissionstoryapp.di.Injection
import com.ipunkpradipta.submissionstoryapp.network.*
import com.ipunkpradipta.submissionstoryapp.ui.StoriesViewModel
import com.ipunkpradipta.submissionstoryapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _loginToken = MutableLiveData<String>()
    val loginToken : LiveData<String> = _loginToken

    fun postRegister(registerRequest: RegisterRequest) = authRepository.postRegister(registerRequest)

    fun login(loginRequest: LoginRequest){
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLogin(loginRequest)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!== null){
                        _snackbarText.value = Event(responseBody.message)
                        _isError.value = responseBody.error
                        _loginToken.value = responseBody.loginResult.token
                    }

                }else{
                    val jsonObject: JSONObject?
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val message = jsonObject.getString("message")
                        _snackbarText.value = Event(message)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _snackbarText.value = Event(t.message + " Please check your internet connection")
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "AuthViewModel"
    }
}