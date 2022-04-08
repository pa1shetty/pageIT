package com.example.pageit.ui.loginscreen

import androidx.lifecycle.ViewModel
import com.example.pageit.data.datastore.DataStoreRepository

class LoginScreenViewModel(private val dataStoreRepository: DataStoreRepository) : ViewModel() {

    suspend fun saveFbUserId(userId: String) {
        dataStoreRepository.saveFbUserId(userId)
    }

    suspend fun saveFbUserAccessToken(fbUserAccessToken: String) {
        dataStoreRepository.saveFbUserAccessToken(fbUserAccessToken)
    }

}