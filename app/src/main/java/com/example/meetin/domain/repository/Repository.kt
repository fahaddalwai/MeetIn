package com.example.meetin.domain.repository

import android.content.Intent
import android.net.Uri
import com.example.meetin.core.util.Resource
import com.example.meetin.domain.model.SignupRequest
import com.example.meetin.domain.model.SignupResponse
import com.example.meetin.domain.model.UserDetailsRequest
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun signup(user: SignupRequest): Flow<Resource<SignupResponse>>

    fun login(user:SignupRequest): Flow<Resource<SignupResponse>>

    fun inputUserDetails(user: UserDetailsRequest)

    fun gsoSignIn(data: Intent?):Flow<Resource<FirebaseUser>>

    fun gsoSignUp(data: Intent?):Flow<Resource<FirebaseUser>>

    fun checkIfUsernameExists(username:String):Flow<Resource<String>>

    fun uploadImage(fileUri: Uri):Flow<Resource<String>>
    
    fun uploadProfileImageToFirebase(url:String)

    fun uploadPersonalDetailsToFirebase(user: UserDetailsRequest):Flow<Resource<String>>

    fun uploadCollegeDetailsToFirebase(user:UserDetailsRequest):Flow<Resource<String>>

}