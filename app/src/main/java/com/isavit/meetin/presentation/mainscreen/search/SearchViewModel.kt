package com.isavit.meetin.presentation.mainscreen.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.model.FriendRequest
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
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError


    private val _userList = MutableLiveData<List<UserDetailsRequest>>()
    val userList: LiveData<List<UserDetailsRequest>>
        get() = _userList


    private val _searchQuery = MutableLiveData("")
    private var searchJob: Job? = null

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            repository.searchForFriends(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _isLoading.value = false
                            _userList.value = result.data as MutableList<UserDetailsRequest>?
                        }
                        is Resource.Error -> {
                            _apiError.value = result.message
                            _isLoading.value = false
                        }
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }.launchIn(this)
        }
    }


    fun addFriend(friend: FriendRequest) {
        viewModelScope.launch {
            repository.addFriend(friend).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _isLoading.value = false
                        val userMainList = _userList.value?.toMutableList()
                        val itr = userMainList?.iterator()
                        if (itr != null) {
                            while (itr.hasNext()) {
                                val user: UserDetailsRequest = itr.next()
                                if (user.email == friend.email) {
                                    itr.remove()
                                }
                            }
                        }

                        if (userMainList != null) {
                            _userList.value = userMainList.toList()
                        }



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

    private fun getFriends() {
        viewModelScope.launch {
            repository.searchFriend().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _userList.value = result.data
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

    init {
        getFriends()
    }
}