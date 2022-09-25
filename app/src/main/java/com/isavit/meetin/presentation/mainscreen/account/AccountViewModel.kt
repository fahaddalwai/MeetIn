package com.isavit.meetin.presentation.mainscreen.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.model.Post
import com.isavit.meetin.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val username = MutableLiveData<String>()

    val aboutMe = MutableLiveData<String>()

    val profileImage = MutableLiveData<String>()

    val collegeDetails = MutableLiveData<String>()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts


    val followersCount = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError

    fun getAccountDetails() {
        viewModelScope.launch {
            repository.getProfileDetails().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        followersCount.value = result.data?.friends?.size.toString() + " Friends"
                        _posts.value = result.data?.posts?.reversed()

                        _isLoading.value = false
                        username.value = result.data?.username
                        aboutMe.value = result.data?.aboutMe
                        profileImage.value = result.data?.profilePic
                        collegeDetails.value =
                            "${result.data?.college}  |  ${result.data?.graduationYear}  | ${result.data?.branch}"


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
        getAccountDetails()

    }
}
