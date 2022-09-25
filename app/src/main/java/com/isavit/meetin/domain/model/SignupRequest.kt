package com.isavit.meetin.domain.model

data class SignupRequest(
    val email: String,
    val password: String = " "
)
