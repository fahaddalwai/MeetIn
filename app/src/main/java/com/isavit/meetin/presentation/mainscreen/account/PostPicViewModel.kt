package com.isavit.meetin.presentation.mainscreen.account

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostPicViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    val caption = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError


    private val _captionEmpty = MutableLiveData<String>()
    val captionEmpty: LiveData<String>
        get() = _captionEmpty

    private val _goBackAccount = MutableLiveData<Boolean>()
    val goBackAccount: LiveData<Boolean>
        get() = _goBackAccount

    fun addImageToFirebase(uri: Uri) {
        if (caption.value.toString().isNotEmpty() && caption.value.toString() != "null") {
            viewModelScope.launch {
                viewModelScope.launch {
                    repository.postImage(uri, caption.value.toString())
                        .onEach { result ->
                            when (result) {
                                is Resource.Success -> {
                                    _isLoading.value = false
                                    _goBackAccount.value = true
                                }
                                is Resource.Loading -> {
                                    _isLoading.value = true
                                    _goBackAccount.value = false
                                }
                                is Resource.Error -> {
                                    _isLoading.value = false
                                    _apiError.value = result.message
                                    _goBackAccount.value = false
                                }
                            }
                        }.launchIn(this)

                }
            }
        } else {
            _captionEmpty.value = "Caption is empty! Enter a caption too"
        }

    }
}