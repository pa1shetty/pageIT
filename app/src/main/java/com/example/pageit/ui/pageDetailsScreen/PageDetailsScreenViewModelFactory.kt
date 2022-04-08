package com.example.pageit.ui.pageDetailsScreen

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pageit.data.database.DatabaseRepository
import com.example.pageit.data.datastore.DataStoreRepository
import com.example.pageit.network.NetworkRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@SuppressLint("CustomSplashScreen")
class PageDetailsScreenViewModelFactory @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val NetworkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PageDetailsScreenViewModel(
            dataStoreRepository, NetworkRepository, databaseRepository
        ) as T
    }
}