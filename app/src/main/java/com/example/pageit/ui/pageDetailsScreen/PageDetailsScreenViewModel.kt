package com.example.pageit.ui.pageDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pageit.data.database.DatabaseRepository
import com.example.pageit.data.database.module.FeedModule
import com.example.pageit.data.datastore.DataStoreRepository
import com.example.pageit.data.module.ErrorCode
import com.example.pageit.data.module.RequestResponse
import com.example.pageit.data.module.StatusEnum
import com.example.pageit.network.NetworkRepository
import com.example.pageit.utils.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageDetailsScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private suspend fun getFbUserAccessToken() = dataStoreRepository.getFbUserAccessToken()

    private val _feedFetchState = MutableStateFlow(RequestResponse())
    val feedFetchState: StateFlow<RequestResponse> = _feedFetchState

    fun getPageDetail(pageId: String) = databaseRepository.getPageDetails(pageId)

    //Fetch feeds from server
    suspend fun fetchFeeds(pageId: String) {
        try {
            _feedFetchState.emit(RequestResponse(status = StatusEnum.LOADING))

            val feeds = networkRepository.getFeeds(pageId, getFbUserAccessToken())
            val feedList: ArrayList<FeedModule> = ArrayList()
            feeds.feed?.data?.forEach { feed ->

                if (feed.story != null || feed.message != null)
                    feedList.add(
                        FeedModule(
                            feed.id,
                            feed.story,
                            feed.message,
                            feed.created_time,
                            pageId
                        )
                    )
            }
            saveFeeds(feedList, pageId)
            _feedFetchState.value = (RequestResponse(StatusEnum.SUCCESS))
        } catch (e: NoInternetException) {
            _feedFetchState.value = RequestResponse(
                StatusEnum.FAILED,
                ErrorCode.NO_INTERNET
            )
        } catch (e: Exception) {
            _feedFetchState.value = RequestResponse(
                StatusEnum.FAILED
            )
        }

    }

    //Save feeds to db
    private fun saveFeeds(feedList: ArrayList<FeedModule>, pageId: String) {
        viewModelScope.launch(Dispatchers.IO) { databaseRepository.saveFeeds(feedList, pageId) }
    }

    fun getFeeds(pageId: String) = databaseRepository.getFeeds(pageId)

}