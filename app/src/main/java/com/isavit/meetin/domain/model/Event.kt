package com.isavit.meetin.domain.model

data class Event(
    val eventName: String,
    val eventImage: String,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val eventBy: String
) {
    constructor() : this("", "", "", "", "", "")
}
