package com.example.pageit.data.module.page.details

import com.google.gson.annotations.SerializedName

data class PageList(
    @SerializedName("data")
    val data: List<PageData>,
    val paging: Paging
)