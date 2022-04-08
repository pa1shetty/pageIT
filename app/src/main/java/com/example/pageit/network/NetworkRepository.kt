package com.example.pageit.network

import com.example.pageit.data.database.module.PageModule
import com.example.pageit.data.module.page.Feeds
import com.example.pageit.data.module.page.details.PageList
import com.example.pageit.data.module.page.pageMoreDetails.PageMoreDetails
import com.example.pageit.data.module.userDetails.UserDetails
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val fbApiService: FbApiService,
    private val beApiService: BeApiService
) : SafeApiRequest() {

    companion object {
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
    }

    suspend fun getUserDetails(fbUserAccessToken: String): UserDetails {
        return gson.fromJson(apiRequest {
            fbApiService.getUserDetails(fbUserAccessToken)
        }, UserDetails::class.java)
    }

    suspend fun getPageList(fbUserAccessToken: String, userId: String): PageList {
        return fbApiService.getPageList(userId, fbUserAccessToken)
    }

    suspend fun fetchPageDetails(pageId: String, fbUserAccessToken: String): PageMoreDetails {
        return gson.fromJson(apiRequest {
            fbApiService.getPageDetails(pageId, fbUserAccessToken)
        }, PageMoreDetails::class.java)
    }

    suspend fun getFeeds(pageId: String, fbUserAccessToken: String): Feeds {
        return gson.fromJson(apiRequest {
            fbApiService.getFeeds(pageId, fbUserAccessToken)
        }, Feeds::class.java)
    }

    fun savePageDetails(pageInfo: PageModule) = beApiService.savePageDetails(pageInfo)


}