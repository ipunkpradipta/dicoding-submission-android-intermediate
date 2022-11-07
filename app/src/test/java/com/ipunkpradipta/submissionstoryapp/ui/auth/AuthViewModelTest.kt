package com.ipunkpradipta.submissionstoryapp.ui.auth

import com.ipunkpradipta.submissionstoryapp.RequestDummy
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

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
//        `when`(authViewModel.register(dummyRegister)).thenReturn(expectedRegister)
    }
}