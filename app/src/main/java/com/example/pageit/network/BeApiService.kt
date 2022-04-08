package com.example.pageit.network

import com.example.pageit.data.database.module.PageModule
import com.example.pageit.data.module.beResponse.BeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BeApiService {
    @Headers("Content-Type: application/json")
    @POST("fb_page_details")
    fun savePageDetails(@Body pageInfo: PageModule): Call<BeResponse>
}