package com.example.pageit.network

import com.example.pageit.data.module.page.details.PageList
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FbApiService {
    @GET("me?fields=name")
    suspend fun getUserDetails(
        @Query("access_token") accessToken: String
    ): Response<JsonObject>


    @GET("{userId}/accounts")
    suspend fun getPageList(
        @Path("userId") userId: String,
        @Query("access_token") accessToken: String
    ): PageList


    @GET("{pageId}?fields=followers_count,cover,picture.type(large),about,access_token,birthday,business,category,company_overview,contact_address,current_location,description,emails,engagement,fan_count,founded,has_whatsapp_business_number,has_whatsapp_number,link,location,name,phone,rating_count,username,website")
    suspend fun getPageDetails(
        @Path("pageId") userId: String,
        @Query("access_token") accessToken: String
    ): Response<JsonObject>

    @GET("{pageId}?fields=feed")
    suspend fun getFeeds(
        @Path("pageId") userId: String,
        @Query("access_token") accessToken: String
    ): Response<JsonObject>
}