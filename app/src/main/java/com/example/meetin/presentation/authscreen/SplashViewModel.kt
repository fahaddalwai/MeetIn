package com.example.meetin.presentation.authscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meetin.core.util.Resource
import com.example.meetin.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private fun checkIfLoggedIn(){
        viewModelScope.launch{
            delay(500)
            repository.logOut().onEach { result->
                when (result) {
                    is Resource.Success -> {
                        _isLoggedIn.value=result.data
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {


                    }
                }
            }.launchIn(this)
        }
    }

    init{
        checkIfLoggedIn()
    }

}