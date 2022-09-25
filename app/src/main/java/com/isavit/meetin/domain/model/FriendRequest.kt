package com.isavit.meetin.domain.model

data class FriendRequest(
    val name: String?,
    val email: String?,
    val username: String = "",
    val profilePic: String = "",
    val university: String = "",
    val posts: List<Post> = emptyList<Post>()
) {
    constructor() : this("", "")
}
