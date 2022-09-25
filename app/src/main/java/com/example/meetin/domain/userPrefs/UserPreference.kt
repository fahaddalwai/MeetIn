package com.example.meetin.domain.userPrefs

interface UserPreference {

    suspend fun setUserExists(exists: Boolean)

    suspend fun getUserStatus(): Boolean

    suspend fun clearPreferences()

}