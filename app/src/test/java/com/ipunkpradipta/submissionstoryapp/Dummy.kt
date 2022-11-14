package com.ipunkpradipta.submissionstoryapp

import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.LoginRequest
import com.ipunkpradipta.submissionstoryapp.data.remote.response.LoginResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.response.LoginResult
import com.ipunkpradipta.submissionstoryapp.data.remote.RegisterRequest

object Dummy {
    fun generateResponseRegister(): DefaultResponse {
        return DefaultResponse(false,"User Created")
    }

    fun generateRequestRegister(): RegisterRequest {
        return RegisterRequest("ipung","ipung.munandar@gmail.com","123456")
    }

    fun generateRequestLogin(): LoginRequest {
        return LoginRequest("bala@gmail.com","123456")
    }

    fun generateResponseLogin(): LoginResponse {
        val loginResult = LoginResult("ini_id_dummy","ipung","tokenNyaKuRAngPanjang")
        return LoginResponse(false,"Success", loginResult)
    }
}