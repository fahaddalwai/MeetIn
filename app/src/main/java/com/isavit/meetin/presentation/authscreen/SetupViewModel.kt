package com.isavit.meetin.presentation.authscreen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.model.UserDetailsRequest
import com.isavit.meetin.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val username = MutableLiveData<String>()

    val aboutMe = MutableLiveData<String>()

    val dateOfBirth = MutableLiveData<String>()

    val gender = MutableLiveData<String>()

    fun setGenderValue(genderType: String) {
        gender.value = genderType
    }


    private var searchJob: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _emptyError = MutableLiveData<String>()
    val emptyError: LiveData<String>
        get() = _emptyError

    private val _isAvailable = MutableLiveData<Boolean>()
    val isAvailable: LiveData<Boolean>
        get() = _isAvailable

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError

    private val _nextPage = MutableLiveData<Boolean>()
    val nextPage: LiveData<Boolean>
        get() = _nextPage

    fun checkUsernameExists() {
        viewModelScope.launch {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(500L)
                repository.checkIfUsernameExists(username.value.toString())
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _isLoading.value = false
                                _isAvailable.value = true
                            }
                            is Resource.Loading -> {
                                _isLoading.value = true

                            }
                            is Resource.Error -> {
                                _isAvailable.value = false
                                _isLoading.value = false
                                _apiError.value = result.message

                            }
                        }
                    }.launchIn(this)

            }

        }
    }

    fun addImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            searchJob = viewModelScope.launch {
                delay(500L)
                repository.uploadImage(uri)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _isLoading.value = false
                            }
                            is Resource.Loading -> {
                                _isLoading.value = true
                            }
                            is Resource.Error -> {
                                _isAvailable.value = false
                                _isLoading.value = false
                                _apiError.value = result.message

                            }
                        }
                    }.launchIn(this)

            }
        }
    }

    fun updateUserDetails() {
        if (!(aboutMe.value.toString().isNullOrEmpty() ||
                    username.value.toString().isNullOrEmpty() ||
                    dateOfBirth.value.toString().isNullOrEmpty())
        )
            viewModelScope.launch {
                repository.uploadPersonalDetailsToFirebase(
                    UserDetailsRequest(
                        name = "",
                        email = "",
                        aboutMe = aboutMe.value.toString(),
                        username = username.value.toString(),
                        dob = dateOfBirth.value.toString(),
                        gender = gender.value.toString()
                    )
                ).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _isLoading.value = false
                            _nextPage.value = true
                        }
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                        is Resource.Error -> {
                            _apiError.value = result.message
                            _isLoading.value = false

                        }
                    }
                }.launchIn(this)

            } else {
            _emptyError.value = "Some fields seem to be empty"
        }
    }


}