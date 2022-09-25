package com.isavit.meetin.presentation.authscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.domain.userPrefs.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val userPreference: UserPreference) :
    ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private fun checkIfLoggedIn() {
        viewModelScope.launch {
            delay(500)
            _isLoggedIn.value = userPreference.getUserStatus()
        }
    }

    init {
        checkIfLoggedIn()
    }

}