package com.example.meetin.remote

import android.content.Intent
import android.util.Log
import com.example.meetin.core.util.Resource
import com.example.meetin.domain.model.SignupRequest
import com.example.meetin.domain.model.SignupResponse
import com.example.meetin.domain.model.UserDetailsRequest
import com.example.meetin.domain.repository.Repository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: Firebase
) : Repository {


    override suspend fun signup(user: SignupRequest): Flow<Resource<SignupResponse>> =
        callbackFlow {
            trySend(Resource.Loading())
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val responseUser = auth.currentUser
                        if (responseUser != null) {
                            trySend(Resource.Success(SignupResponse(responseUser.uid)))
                        }

                    } else {
                        trySend(Resource.Error("Error Signing user in"))
                    }
                }
            awaitClose { }
        }

    override suspend fun checkIfUserExists(): Flow<Resource<Boolean>> =
        callbackFlow {
            trySend(Resource.Loading())
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    trySend(Resource.Success(false))
                } else {
                    trySend(Resource.Success(true))
                }
            } catch (e: HttpException) {
                trySend(Resource.Error(e.toString()))
            } catch (e: IOException) {
                trySend(Resource.Error(e.toString()))
            }
            awaitClose { }
        }

    override suspend fun login(user: SignupRequest): Flow<Resource<SignupResponse>> =
        callbackFlow {
            trySend(Resource.Loading())
            auth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val responseUser = auth.currentUser
                        if (responseUser != null) {
                            trySend(Resource.Success(SignupResponse(responseUser.uid)))
                        }
                    } else {
                        trySend(Resource.Error(task.exception.toString()))
                    }
                }
            awaitClose { }
        }

    override suspend fun inputUserDetails(user: UserDetailsRequest): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading())
            auth.uid?.let {
                db.firestore.collection("users").document(it)
                    .set(user)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Success"))
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error("Unable to process request"))
                    }
            }
            awaitClose { }
        }

    override suspend fun gsoSignUp(data: Intent?):Flow<Resource<FirebaseUser>> =callbackFlow {
        trySend(Resource.Loading())
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        trySend(Resource.Success(user))
                    } else {
                        trySend(Resource.Error(task.exception.toString()))

                    }
                }
        } catch (e: ApiException) {
            trySend(Resource.Error(e.message.toString()))
            Log.i("exception",e.toString())
        }
        awaitClose {  }
    }




}
