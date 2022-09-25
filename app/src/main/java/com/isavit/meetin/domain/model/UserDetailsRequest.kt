package com.isavit.meetin.domain.model

data class UserDetailsRequest(
    val name: String?,
    val email: String?,
    val username: String = "",
    val aboutMe: String = "",
    val college: String = "",
    val joinedYear: String = "",
    val graduationYear: String = "",
    val branch: String = "",
    val profilePic: String = "",
    val dob: String = "",
    val gender: String = "",
    val friends: List<FriendRequest> = emptyList<FriendRequest>(),
    val posts: List<Post> = emptyList<Post>(),
    val friendsPostToSee: List<Post> = emptyList<Post>(),
) {
    constructor() : this("", "")
}
