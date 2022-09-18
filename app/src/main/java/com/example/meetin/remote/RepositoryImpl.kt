package com.example.meetin.remote

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.meetin.core.util.Resource
import com.example.meetin.domain.model.*
import com.example.meetin.domain.repository.Repository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: Firebase,
    private val storage: FirebaseStorage
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
    override fun inputUserDetails(user: UserDetailsRequest) {
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
                } else {
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
                    Log.i("Document no exist", "")
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
                } else {
                    trySend(Resource.Error("Error"))
                }
            }
        awaitClose { }
    }

    override fun checkIfUsernameExists(username: String): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        val docRef = db.firestore.collection("usernames").document(username)
        docRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    Log.i("Document no exist", "")
                    try {
                        trySend(Resource.Success("User exists"))
                    } catch (e: Exception) {
                        trySend(Resource.Error(e.toString()))
                    }
                } else {
                    trySend(Resource.Error("Error"))
                }
            }
        awaitClose { }
    }

    override fun uploadImage(fileUri: Uri): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = storage.reference.child("images/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.i("imageurl", imageUrl)
                    uploadProfileImageToFirebase(imageUrl)
                    trySend(Resource.Success(imageUrl))
                }
            }
            .addOnFailureListener { e ->
                trySend(Resource.Error(e.toString()))
            }
        awaitClose { }
    }

    private fun uploadImageInternal(fileUri: Uri): String {
        var imageUrl = ""
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = storage.reference.child("images/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    Log.i("SHow mee", it.toString())

                    imageUrl = it.toString()
                    Log.i("SHow mee", imageUrl)
                }
            }
            .addOnFailureListener {
                Log.i("We have a failer", "psadsaf")
            }
        return imageUrl
    }

    override fun uploadProfileImageToFirebase(url: String) {

        val email: String = auth.currentUser?.email.toString()
        if (email != "") {
            db.firestore.collection("users").document(email)
                .update("profilePic", url)
        }

    }

    override fun uploadPersonalDetailsToFirebase(user: UserDetailsRequest): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading())
            val email: String = auth.currentUser?.email.toString()
            if (email != "") {

                db.firestore.collection("usernames").document(user.username)
                    .set(
                        mapOf(
                            "user" to user.username
                        )
                    )


                db.firestore.collection("users").document(email)
                    .update(
                        mapOf(
                            "aboutMe" to user.aboutMe,
                            "username" to user.username,
                            "dob" to user.dob,
                            "gender" to user.gender
                        )
                    )
                trySend(Resource.Success("Success"))

            } else {
                trySend(Resource.Error("Could not find the email address"))
            }
            awaitClose { }
        }

    override fun uploadCollegeDetailsToFirebase(user: UserDetailsRequest): Flow<Resource<String>> =
        callbackFlow {



            trySend(Resource.Loading())
            val email: String = auth.currentUser?.email.toString()
            if (email != "") {



                db.firestore.collection("users").document(email)
                    .update(
                        mapOf(
                            "branch" to user.branch,
                            "college" to user.college,
                            "graduationYear" to user.graduationYear,
                            "joinedYear" to user.joinedYear
                        )
                    )
                trySend(Resource.Success("Success"))

            } else {
                trySend(Resource.Error("Could not find the email address"))
            }
            awaitClose { }
        }

    override fun getProfileDetails(): Flow<Resource<UserDetailsRequest>> = callbackFlow {
        trySend(Resource.Loading())
        val email: String = auth.currentUser?.email.toString()
        if (email != "") {
            val docRef = db.firestore.collection("users").document(email)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        try {
                            val userObject = document.toObject<UserDetailsRequest>()
                            trySend(Resource.Success(userObject))
                        } catch (e: Exception) {
                            trySend(Resource.Error(e.toString()))
                        }
                    } else {
                        trySend(Resource.Error("Error"))
                    }
                }
        } else {
            trySend(Resource.Error("Email does not exist"))
        }
        awaitClose { }
    }

    override fun postImage(fileUri: Uri, caption: String): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        val email: String = auth.currentUser?.email.toString()

        if (email != "") {

            var imageUrl = ""
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val refStorage = storage.reference.child("images/$fileName")
            refStorage.putFile(fileUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        val docRef = db.firestore.collection("users").document(email)

                        //adding to other users document
                            docRef.get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        try {
                                            val userObject = document.toObject<UserDetailsRequest>()
                                            if (userObject != null) {
                                                db.firestore.collection("users").document(email)
                                                    .update("posts", FieldValue.arrayUnion(
                                                        userObject.name?.let { it1 ->
                                                            Post(imageUrl, caption,
                                                                it1,userObject.college,userObject.profilePic)
                                                        }))
                                            }


                                            if (userObject != null) {
                                                for(friend in userObject.friends){
                                                    friend.email?.let { friendEmail ->
                                                        db.firestore.collection("users").document(
                                                            friendEmail
                                                        )
                                                            .update("friends", FieldValue.arrayUnion(FriendRequest(userObject.name,userObject.email,userObject.username,userObject.profilePic)))

                                                        db.firestore.collection("users").document(
                                                            friendEmail
                                                        )
                                                            .update("friendsPostToSee", FieldValue.arrayUnion(
                                                                userObject.name?.let { it1 ->
                                                                    Post(imageUrl, caption,
                                                                        it1,userObject.college,userObject.profilePic)
                                                                }))
                                                    }
                                                }
                                                db.firestore.collection("users").document(
                                                    email
                                                )
                                                    .update("friendsPostToSee", FieldValue.arrayUnion(
                                                        userObject.name?.let { it1 ->
                                                            Post(imageUrl,caption,
                                                                it1,userObject.college,userObject.profilePic)
                                                        }))


                                            }


                                        } catch (e: Exception) {
                                            trySend(Resource.Error(e.toString()))
                                        }
                                    } else {
                                        trySend(Resource.Error("Error"))
                                    }
                                }



                        trySend(Resource.Success("Image has been successfully uploaded"))

                    }
                }
                .addOnFailureListener {
                    Log.i("We have a failer", "psadsaf")
                }


        } else {
            trySend(Resource.Error("Could not find the email address"))
        }

        awaitClose { }
    }


    override fun searchFriend(): Flow<Resource<List<UserDetailsRequest>>> =
        callbackFlow {
            trySend(Resource.Loading())
            val email: String = auth.currentUser?.email.toString()

            val friends= mutableListOf<UserDetailsRequest>()
            if (email != "") {

                val docRef = db.firestore.collection("users")
                docRef
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val userObject = document.toObject<UserDetailsRequest>()
                            if(userObject.email!=email) {
                                friends.add(userObject)
                                Log.i("my user email",userObject.email.toString())
                            }
                        }

                        trySend(Resource.Success(friends))
                    }

                    .addOnFailureListener { exception ->
                        trySend(Resource.Error(exception.toString()))
                    }


                awaitClose { }
            }
        }

    override fun addFriend(friend: FriendRequest): Flow<Resource<String>> =callbackFlow{
        trySend(Resource.Loading())
        val email: String = auth.currentUser?.email.toString()

        if (email != "") {
            db.firestore.collection("users").document(email)
                .update("friends", FieldValue.arrayUnion(friend))

            for(post in friend.posts){
                db.firestore.collection("users").document(email)
                    .update("friendsPostToSee", FieldValue.arrayUnion(post))
            }



            trySend(Resource.Success("Success"))

        } else {
            trySend(Resource.Error("Could not find the email address"))
        }
        awaitClose { }
    }

    override fun showFriendsPosts():Flow<Resource<List<Post>>> = callbackFlow {
        trySend(Resource.Loading())
        val email: String = auth.currentUser?.email.toString()

        if (email != "") {
            val docRef = db.firestore.collection("users").document(email)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        try {
                            val userObject = document.toObject<UserDetailsRequest>()
                            if (userObject != null) {

                                trySend(Resource.Success(userObject.friendsPostToSee.reversed()))
                            }
                        } catch (e: Exception) {
                            trySend(Resource.Error(e.toString()))
                        }
                    } else {
                        trySend(Resource.Error("Error"))
                    }
                }
        } else {
            trySend(Resource.Error("Could not find the email address"))
        }

        awaitClose { }
    }

    override fun searchForFriends(friend: String): Flow<Resource<List<UserDetailsRequest>>> =callbackFlow {
        val email: String = auth.currentUser?.email.toString()
        trySend(Resource.Loading())
        val usersThatMatch= mutableListOf<UserDetailsRequest>()
        if (email != "") {

            db.firestore.collection("users").orderBy("name").startAt(friend).endAt("$friend\uf8ff")
                .get().addOnCompleteListener{
                    if(it.isSuccessful){
                        for (user in it.result.toObjects(UserDetailsRequest::class.java)){
                            if(user.email!=email){
                                usersThatMatch.add(user)
                            }
                        }
                        trySend(Resource.Success(usersThatMatch))
                    }else{
                        trySend(Resource.Error(it.exception.toString()))
                    }
                }

            awaitClose { }
        }
    }

    override fun getAllEvents(): Flow<Resource<List<Event>>> =callbackFlow {
        trySend(Resource.Loading())
        val eventList= mutableListOf<Event>()
        db.firestore.collection("events")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val eventObject = document.toObject<Event>()
                    eventList.add(eventObject)
                }
                trySend(Resource.Success(eventList))
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Error(exception.toString()))
            }
        awaitClose {  }
    }

    override fun logOut(): Flow<Resource<Boolean>> =callbackFlow {
        trySend(Resource.Loading())
        try{
            Firebase.auth.signOut()
            trySend(Resource.Success(true))
        }catch (e:Exception){
            trySend(Resource.Error(e.toString()))
        }
        awaitClose {  }
    }

    override fun checkIfLoggedIn(): Flow<Resource<Boolean>> =callbackFlow{
        trySend(Resource.Loading())
        try{
            val user = Firebase.auth.currentUser
            if (user != null) {
                trySend(Resource.Success(true))
            } else {
                trySend(Resource.Success(false))
            }
        }catch (e:Exception){
            trySend(Resource.Error(e.toString()))
        }
        awaitClose {  }
    }
}




