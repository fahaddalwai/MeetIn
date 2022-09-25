package com.isavit.meetin.presentation.mainscreen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.model.Post
import com.isavit.meetin.domain.repository.Repository
import com.isavit.meetin.domain.userPrefs.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private val _postsList = MutableLiveData<List<Post>>()
    val postsList: LiveData<List<Post>>
        get() = _postsList

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean>
        get() = _logout


    fun getFriendsPosts() {
        viewModelScope.launch {
            repository.showFriendsPosts().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _postsList.value = result.data
                        _isLoading.value = false
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
        }
    }

    fun logOut() {
        viewModelScope.launch {
            userPreference.setUserExists(false)
            _logout.value = true
        }
    }

    init {
        getFriendsPosts()
    }
}