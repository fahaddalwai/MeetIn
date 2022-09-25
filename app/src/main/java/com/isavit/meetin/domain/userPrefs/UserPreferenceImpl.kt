package com.isavit.meetin.domain.userPrefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.isavit.meetin.core.util.Constants.USER_PREF_NAME
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(USER_PREF_NAME)

class UserPreferenceImpl @Inject constructor(
    private val context: Context
) : UserPreference {

    override suspend fun setUserExists(exists: Boolean) {
        context.dataStore.edit {
            it[USER_EXISTS] = exists
        }
    }

    override suspend fun getUserStatus(): Boolean {
        return (context.dataStore.data
            .map { userDetail ->
                val userExists = userDetail[USER_EXISTS] ?: false
                userExists
            }).first()
    }


    override suspend fun clearPreferences() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        val USER_EXISTS = booleanPreferencesKey("USER_EXISTS")


    }

}