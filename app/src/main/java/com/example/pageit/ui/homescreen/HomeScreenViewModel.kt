package com.example.pageit.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pageit.data.database.DatabaseRepository
import com.example.pageit.data.database.module.PageModule
import com.example.pageit.data.datastore.DataStoreRepository
import com.example.pageit.data.module.ErrorCode
import com.example.pageit.data.module.RequestResponse
import com.example.pageit.data.module.StatusEnum
import com.example.pageit.data.module.StatusEnum.LOADING
import com.example.pageit.data.module.StatusEnum.SUCCESS
import com.example.pageit.data.module.page.pageMoreDetails.PageMoreDetails
import com.example.pageit.network.NetworkRepository
import com.example.pageit.utils.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _pageFetchState = MutableStateFlow(RequestResponse())
    val pageFetchState: StateFlow<RequestResponse> = _pageFetchState

    fun getUserName() = dataStoreRepository.getUserName()
    private suspend fun getFbUserAccessToken() = dataStoreRepository.getFbUserAccessToken()
    private suspend fun getFbUserId() = dataStoreRepository.getFbUserId()

    fun getPages() = databaseRepository.getPages()
    private suspend fun fetchPageDetails(pageId: String, userAccessToken: String): PageMoreDetails {
        return networkRepository.fetchPageDetails(pageId, userAccessToken)
    }

    //Fetch page list
    suspend fun fetchPages() {
        val pageModules: ArrayList<PageModule> = ArrayList()
        try {
            _pageFetchState.emit(RequestResponse(status = LOADING))
            networkRepository.getPageList(
                getFbUserAccessToken(),
                getFbUserId()
            ).data.forEach { page ->
                fetchPageDetails(page.id, getFbUserAccessToken()).apply {
                    pageModules.add(
                        PageModule(
                            id,
                            about,
                            access_token,
                            birthday,
                            business,
                            category,
                            company_overview,
                            contact_address,
                            cover?.source,
                            current_location,
                            description,
                            emails.toString(),
                            engagement.toString(),
                            fan_count,
                            followers_count,
                            founded,
                            has_whatsapp_business_number,
                            has_whatsapp_number,
                            link,
                            location.toString(),
                            name, phone, picture?.data?.url, rating_count, username, website
                        )
                    )
                }
            }
            savePages(pageModules)
            _pageFetchState.emit(RequestResponse(SUCCESS))
        } catch (e: NoInternetException) {
            _pageFetchState.emit(
                RequestResponse(
                    StatusEnum.FAILED,
                    ErrorCode.NO_INTERNET, e.toString()
                )
            )
        } catch (e: Exception) {
            _pageFetchState.emit(
                RequestResponse(
                    StatusEnum.FAILED, errorMessage = e.toString()
                )
            )
        }
    }

    //To save page details in db
    private fun savePages(pageModules: java.util.ArrayList<PageModule>) {
        viewModelScope.launch(Dispatchers.IO) { databaseRepository.savePages(pageModules) }
    }

    //Get user details from fb api.
    suspend fun getUserDetails() {
        try {
            networkRepository.getUserDetails(getFbUserAccessToken()).name?.let { name ->
                dataStoreRepository.saveUserName(
                    name
                )
            }
        } catch (e: NoInternetException) {

        } catch (e: Exception) {

        }


    }

}