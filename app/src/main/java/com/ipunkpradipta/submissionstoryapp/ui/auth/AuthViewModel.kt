package com.ipunkpradipta.submissionstoryapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ipunkpradipta.submissionstoryapp.network.*
import com.ipunkpradipta.submissionstoryapp.utils.Event
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _loginToken = MutableLiveData<String>()
    val loginToken : LiveData<String> = _loginToken

    fun register(registerRequest: RegisterRequest){
        _isLoading.value = true
        val client = ApiConfig.getApiService().postRegister(registerRequest)
        client.enqueue(object: Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>,
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!== null){
                        _snackbarText.value = Event(responseBody.message)
                        _isError.value = responseBody.error
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

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                _snackbarText.value = Event(t.message + " Please check your internet connection")
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

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