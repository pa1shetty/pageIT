package com.example.pageit.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val fbUserAccessToken = stringPreferencesKey("fbUserAccessToken")
    val fbUserId = stringPreferencesKey("fbUserId")
    val fbUserName = stringPreferencesKey("fbUserName")

}