package com.isavit.meetin.presentation.authscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.model.UserDetailsRequest
import com.isavit.meetin.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupAccountUniversityViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val college = MutableLiveData<String>()

    val joinedYear = MutableLiveData<String>()

    val graduationYear = MutableLiveData<String>()

    val branch = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _emptyError = MutableLiveData<String>()
    val emptyError: LiveData<String>
        get() = _emptyError


    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError

    private val _nextPage = MutableLiveData<Boolean>()
    val nextPage: LiveData<Boolean>
        get() = _nextPage


    fun updateUserDetails() {
        if (!(college.value.toString().isNullOrEmpty() &&
                    joinedYear.value.toString().isNullOrEmpty() &&
                    branch.value.toString().isNullOrEmpty() &&
                    branch.value.toString().isNullOrEmpty() &&
                    graduationYear.value.toString().isNullOrEmpty())
        )
            viewModelScope.launch {
                repository.uploadCollegeDetailsToFirebase(
                    UserDetailsRequest(
                        name = "",
                        email = "",
                        college = college.value.toString(),
                        joinedYear = joinedYear.value.toString(),
                        branch = branch.value.toString(),
                        graduationYear = graduationYear.value.toString()
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
            _emptyError.value = "Some fields seem to be empty!"
        }
    }

}