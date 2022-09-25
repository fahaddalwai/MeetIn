package com.isavit.meetin.presentation.authscreen

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.core.util.isValidEmail
import com.isavit.meetin.domain.model.SignupRequest
import com.isavit.meetin.domain.repository.Repository
import com.isavit.meetin.domain.userPrefs.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val repository: Repository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    private val _eventGoToHome = MutableLiveData<Boolean>()
    val eventGoToHome: LiveData<Boolean>
        get() = _eventGoToHome


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()


    private fun isValidEmailAndPassword(
        email: String,
        password: String
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

        return true
    }

    fun loginUser() {
        if (isValidEmailAndPassword(
                username.value.toString(),
                password.value.toString(),
            )
        ) {
            viewModelScope.launch {
                repository.login(
                    SignupRequest(
                        username.value.toString().trim(),
                        password.value.toString().trim()
                    )
                ).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _isLoading.value = false
                            _eventGoToHome.value = true
                            userPreference.setUserExists(true)
                        }
                        is Resource.Error -> {
                            _isLoading.value = false
                            _apiError.value = result.message.toString()


                        }
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }.launchIn(this)
            }
        }


    }


    fun gsoLogin(data: Intent) {
        viewModelScope.launch {
            repository.gsoSignIn(data)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _isLoading.value = false
                            _eventGoToHome.value = true
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