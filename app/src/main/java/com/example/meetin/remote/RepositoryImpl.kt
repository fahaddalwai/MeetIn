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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: Firebase
) : Repository {


    /**check that user does not exist in the database. if does not exist then let user make account and enter into database*/
    override fun signup(user: SignupRequest): Flow<Resource<SignupResponse>> =
        callbackFlow {
            trySend(Resource.Loading())

            val docRef = db.firestore.collection("users").document(user.email)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        auth.createUserWithEmailAndPassword(user.email, user.name)
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    val responseUser = auth.currentUser
                                    if (responseUser != null) {
                                        trySend(Resource.Success(SignupResponse(responseUser.uid)))
                                        inputUserDetails(UserDetailsRequest(user.name, user.email))
                                    }

                                }
                            }
                    } else {
                        trySend(Resource.Error("User already exists, please login instead"))
                    }
                }



            awaitClose { }
        }




    /**Inputs the user detail into firestore database*/
    override fun inputUserDetails(user: UserDetailsRequest){
        user.email?.let {
            db.firestore.collection("users").document(it)
                .set(user)
        }
    }


    /**Logs the user into app if they exist*/
    override fun login(user: SignupRequest): Flow<Resource<SignupResponse>> =
        callbackFlow {
            trySend(Resource.Loading())

            val docRef = db.firestore.collection("users").document(user.email)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        auth.signInWithEmailAndPassword(user.email, user.name)
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    val responseUser = auth.currentUser
                                    if (responseUser != null) {
                                        trySend(Resource.Success(SignupResponse(responseUser.uid)))
                                    }

                                } else {
                                    trySend(Resource.Error(task.exception.toString()))
                                    Log.i("loginerror", task.exception.toString())
                                }
                            }
                    }
                }

            awaitClose { }
        }


    /**Checks if user already exists and signs in using google*/
    override fun gsoSignIn(data: Intent?): Flow<Resource<FirebaseUser>> = callbackFlow {
        trySend(Resource.Loading())

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        val docRef = db.firestore.collection("users").document(task.result.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
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
                        Log.i("exception", e.toString())
                    }
                }else{
                    trySend(Resource.Error("Error"))
                }
            }
        awaitClose { }

    }


    /**checks if user exists and if not then allows to make new account and sign up*/
    override fun gsoSignUp(data: Intent?): Flow<Resource<FirebaseUser>> = callbackFlow {
        trySend(Resource.Loading())

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        val docRef = db.firestore.collection("users").document(task.result.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    Log.i("Document no exist","")
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!


                        val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    inputUserDetails(
                                        UserDetailsRequest(
                                            account.givenName,
                                            account.email
                                        )
                                    )
                                    // Sign in success, update UI with the signed-in user's information
                                    val user = auth.currentUser
                                    trySend(Resource.Success(user))
                                } else {
                                    trySend(Resource.Error(task.exception.toString()))

                                }
                            }


                    } catch (e: Exception) {
                        trySend(Resource.Error(e.toString()))
                    }
                }else{
                    trySend(Resource.Error("Error"))
                }
            }
        awaitClose { }
    }


}
