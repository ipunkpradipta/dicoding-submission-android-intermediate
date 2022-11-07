package com.ipunkpradipta.submissionstoryapp.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ipunkpradipta.submissionstoryapp.Dummy
import com.ipunkpradipta.submissionstoryapp.data.AuthRepository
import com.ipunkpradipta.submissionstoryapp.data.Result
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.network.LoginResponse
import com.ipunkpradipta.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var authViewModel: AuthViewModel
    private val dummyResponseRegister = Dummy.generateResponseRegister()
    private val dummyRequestRegister = Dummy.generateRequestRegister()
    private val dummyRequestLogin = Dummy.generateRequestLogin()
    private val dummyResponseLogin = Dummy.generateResponseLogin()

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(authRepository)
    }


    @Test
    fun `when Register Is Success Return Result Success`() = run{
        val expected = MutableLiveData<Result<DefaultResponse>>()
        expected.value = Result.Success(dummyResponseRegister)
        `when`(authRepository.postRegister(dummyRequestRegister)).thenReturn(expected)

        val actualResponse = authViewModel.postRegister(dummyRequestRegister).getOrAwaitValue()

        Mockito.verify(authRepository).postRegister(dummyRequestRegister)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertFalse(actualResponse is Result.Error)
    }

    @Test
    fun `when Register Failed Return Result Error`() = run{
        val expected = MutableLiveData<Result<DefaultResponse>>()
        expected.value = Result.Error("ERROR")
        `when`(authRepository.postRegister(dummyRequestRegister)).thenReturn(expected)

        val actualResponse = authViewModel.postRegister(dummyRequestRegister).getOrAwaitValue()

        Mockito.verify(authRepository).postRegister(dummyRequestRegister)
        Assert.assertTrue(actualResponse is Result.Error)
        Assert.assertFalse(actualResponse is Result.Success)
    }

    @Test
    fun `when Login Success return ResultSuccess`(){
        val expected = MutableLiveData<Result<LoginResponse>>()
        expected.value = Result.Success(dummyResponseLogin)
        `when`(authRepository.postLogin(dummyRequestLogin)).thenReturn(expected)
        val actualResponse = authViewModel.postLogin(dummyRequestLogin).getOrAwaitValue()
        Mockito.verify(authRepository).postLogin(dummyRequestLogin)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Login Failed return ResultError`(){
        val expected = MutableLiveData<Result<LoginResponse>>()
        expected.value = Result.Error("NETWORK_ERROR")
        `when`(authRepository.postLogin(dummyRequestLogin)).thenReturn(expected)
        val actualResponse = authViewModel.postLogin(dummyRequestLogin).getOrAwaitValue()
        Mockito.verify(authRepository).postLogin(dummyRequestLogin)
        Assert.assertTrue(actualResponse is Result.Error)
    }

//    @Test
//    fun `when getToken Is Not Empty`(){
//        val expected:LiveData<String> = "tokenNyaKuRAngPanjang"
//        `when`(authRepository.getTokenAuth()).thenReturn(expected)
//
//        val actualResponse = authViewModel.getTokenAuth().getOrAwaitValue()
//        Mockito.verify(authRepository).getTokenAuth()
//        Assert.assertEquals(expected,actualResponse)
//    }
}