package com.isavit.meetin.domain.repository

import android.content.Intent
import android.net.Uri
import com.isavit.meetin.core.util.Resource
import com.isavit.meetin.domain.model.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun signup(user: SignupRequest): Flow<Resource<SignupResponse>>

    fun login(user: SignupRequest): Flow<Resource<SignupResponse>>

    fun inputUserDetails(user: UserDetailsRequest)

    fun gsoSignIn(data: Intent?): Flow<Resource<FirebaseUser>>

    fun gsoSignUp(data: Intent?): Flow<Resource<FirebaseUser>>

    fun checkIfUsernameExists(username: String): Flow<Resource<String>>

    fun uploadImage(fileUri: Uri): Flow<Resource<String>>

    fun uploadProfileImageToFirebase(url: String)

    fun uploadPersonalDetailsToFirebase(user: UserDetailsRequest): Flow<Resource<String>>

    fun uploadCollegeDetailsToFirebase(user: UserDetailsRequest): Flow<Resource<String>>

    fun getProfileDetails(): Flow<Resource<UserDetailsRequest>>

    fun postImage(fileUri: Uri, caption: String): Flow<Resource<String>>

    fun searchFriend(): Flow<Resource<List<UserDetailsRequest>>>

    fun addFriend(friend: FriendRequest): Flow<Resource<String>>

    fun showFriendsPosts(): Flow<Resource<List<Post>>>

    fun searchForFriends(friend: String): Flow<Resource<List<UserDetailsRequest>>>

    fun getAllEvents(): Flow<Resource<List<Event>>>

    fun logOut(): Flow<Resource<Boolean>>

    fun checkIfLoggedIn(): Flow<Resource<Boolean>>
}