package com.isavit.meetin.presentation.mainscreen.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpandedPostViewModel @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {

    private val _caption = MutableLiveData<String>()
    val caption: LiveData<String>
        get() = _caption

    private val _postUrl = MutableLiveData<String>()
    val postUrl: LiveData<String>
        get() = _postUrl

    private val _postLikes = MutableLiveData<Int>()

    init {
        state.get<String>("url")?.let {
            _postUrl.value = it
        }


        state.get<String>("caption")?.let {
            _caption.value = it
        }

    }
}