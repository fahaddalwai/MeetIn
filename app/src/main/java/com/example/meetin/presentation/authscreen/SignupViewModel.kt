package com.example.meetin.presentation.authscreen

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meetin.core.util.Resource
import com.example.meetin.core.util.isValidEmail
import com.example.meetin.domain.model.SignupRequest
import com.example.meetin.domain.repository.Repository
import com.example.meetin.remote.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    private val _passwordMisMatchError = MutableLiveData<String>()
    val passwordMisMatchError: LiveData<String>
        get() = _passwordMisMatchError

    private val _eventGoToSetup = MutableLiveData<Boolean>()
    val eventGoToSetup: LiveData<Boolean>
        get() = _eventGoToSetup


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val username=MutableLiveData<String>()

    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val repeatedPassword = MutableLiveData<String>()







    private fun isValidEmailAndPassword(
        email: String,
        password: String,
        passwordSecond: String
    ): Boolean {

        if (email.isValidEmail()) {
            _emailError.value = ""
        } else {
            _emailError.value = "Invalid email"
            return false
        }
        if (password.isNotEmpty()) {
            _passwordError.value = ""
        } else {
            _passwordError.value = "Cannot be empty"
            return false
        }
        if (!password.equals(passwordSecond)) {
            _passwordMisMatchError.value = "Passwords don't match"
        } else {
            _passwordMisMatchError.value = ""
        }
        return true
    }

    fun signInUser() {
        if (isValidEmailAndPassword(
                email.value.toString(),
                password.value.toString(),
                repeatedPassword.value.toString()
            )
        ) {
            viewModelScope.launch {
                repository.signup(
                    SignupRequest(
                        email.value.toString().trim(),
                        password.value.toString().trim()
                    )
                ).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _isLoading.value = false
                            _eventGoToSetup.value=true
                        }
                        is Resource.Error -> {
                            _isLoading.value = false

                        }
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }.launchIn(this)
            }
        }


    }


    fun gsoLogin(data: Intent){
        viewModelScope.launch {
            repository.gsoSignUp(data)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _isLoading.value = false
                            _eventGoToSetup.value= true
                            Log.i("Show me valu","show")
                        }
                        is Resource.Error -> {
                            _isLoading.value = false
                            Log.i("heyy",result.message.toString())
                        }
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }.launchIn(this)
        }
    }




}