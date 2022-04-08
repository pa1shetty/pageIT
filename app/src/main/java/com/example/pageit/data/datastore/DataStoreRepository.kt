package com.example.pageit.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.pageit.utils.AppConstants.dataStoreDefaultValue
import com.example.pageit.utils.AppConstants.dataStoreName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(dataStoreName)

class DataStoreRepository @Inject constructor(
    context: Context,
) {
    private val dataStore = context.dataStore

    private suspend fun saveStringData(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preference ->
            preference[key] = value
        }
    }


    suspend fun saveFbUserAccessToken(fbUserAccessToken: String) {
        saveStringData(PreferencesKeys.fbUserAccessToken, fbUserAccessToken)
    }

    suspend fun saveFbUserId(fbUserId: String) {
        saveStringData(PreferencesKeys.fbUserId, fbUserId)
    }

    suspend fun getFbUserAccessToken() = getStringData(
        PreferencesKeys.fbUserAccessToken
    )

    suspend fun getFbUserId() = getStringData(
        PreferencesKeys.fbUserId
    )

    suspend fun saveUserName(fbUserName: String) {
        saveStringData(PreferencesKeys.fbUserName, fbUserName)
    }

    fun getUserName() = getStringAsFlow(PreferencesKeys.fbUserName)

    private suspend fun getStringData(
        key: Preferences.Key<String>,
        defaultValue: String = dataStoreDefaultValue
    ): String {
        val preferences = dataStore.data.first()
        var value = defaultValue
        preferences[key]?.let {
            value = it
        }
        return value
    }


    private fun getStringAsFlow(
        key: Preferences.Key<String>,
        defaultValue: String = dataStoreDefaultValue
    ): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            it[key]?.let { it1 -> (it1) } ?: defaultValue
        }

}