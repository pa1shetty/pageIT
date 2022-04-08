package com.example.pageit.data.module.page.details

data class PageData(
    val access_token: String,
    val category: String,
    val category_list: List<Category>,
    val id: String,
    val name: String,
    val tasks: List<String>
)