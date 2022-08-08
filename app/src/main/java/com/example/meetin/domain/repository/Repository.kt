package com.example.meetin.domain.repository

import android.content.Intent
import com.example.meetin.core.util.Resource
import com.example.meetin.domain.model.SignupRequest
import com.example.meetin.domain.model.SignupResponse
import com.example.meetin.domain.model.UserDetailsRequest
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun signup(user: SignupRequest): Flow<Resource<SignupResponse>>

    suspend fun checkIfUserExists():Flow<Resource<Boolean>>

    suspend fun login(user:SignupRequest): Flow<Resource<SignupResponse>>

    suspend fun inputUserDetails(user: UserDetailsRequest): Flow<Resource<String>>

    suspend fun gsoSignUp(data: Intent?):Flow<Resource<FirebaseUser>>
}