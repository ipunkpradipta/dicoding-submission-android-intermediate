package com.ipunkpradipta.submissionstoryapp.ui.auth

import com.ipunkpradipta.submissionstoryapp.RequestDummy
import com.ipunkpradipta.submissionstoryapp.StoriesDummy
import com.ipunkpradipta.submissionstoryapp.network.ApiService
import com.ipunkpradipta.submissionstoryapp.network.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest
import com.ipunkpradipta.submissionstoryapp.ui.StoriesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest{

    @Mock
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        authViewModel = AuthViewModel()
    }


    @Test
    fun `when Register Is Success Return isError false`() {
        val dummyRegister = RequestDummy.generateRequestRegister()
        val expectedRegister= DefaultResponse(false,"Register Success")
        `when`(authViewModel.register(dummyRegister)).thenReturn(expectedRegister)
    }
}

class FakeApi:ApiService{
    override fun postRegister(body: RegisterRequest): Call<DefaultResponse> {
        TODO("Not yet implemented")
    }
}