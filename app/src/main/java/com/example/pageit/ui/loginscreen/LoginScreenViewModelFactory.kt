package com.example.pageit.ui.loginscreen

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pageit.data.datastore.DataStoreRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@SuppressLint("CustomSplashScreen")
class LoginScreenViewModelFactory @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginScreenViewModel(
            dataStoreRepository
        ) as T
    }
}