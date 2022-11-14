package com.ipunkpradipta.submissionstoryapp.ui.auth

import androidx.lifecycle.*
import com.ipunkpradipta.submissionstoryapp.data.AuthRepository
import com.ipunkpradipta.submissionstoryapp.data.remote.LoginRequest
import com.ipunkpradipta.submissionstoryapp.data.remote.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    fun postRegister(registerRequest: RegisterRequest) = authRepository.postRegister(registerRequest)

    fun postLogin(loginRequest: LoginRequest) = authRepository.postLogin(loginRequest)

    fun saveTokenAuth(token:String){
      viewModelScope.launch {
          authRepository.saveTokenAuth(token)
      }
    }

    fun deleteTokenAuth(){
        viewModelScope.launch {
            authRepository.deleteTokenAuth()
        }
    }

    fun getTokenAuth():LiveData<String> = authRepository.getTokenAuth()

}