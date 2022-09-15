package com.example.meetin.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Post(
    val postUrl:String="",
    val caption:String="",
    val name:String="",
    val university:String="",
    val profileUrl:String=""
)
