package com.example.pageit.data.database.module

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page")
data class PageModule(
    @PrimaryKey(autoGenerate = false)
    var page_id: String,
    val about: String?,
    val access_token: String?,
    val birthday: String?,
    val business: String?,
    val category: String?,
    val company_overview: String?,
    val contact_address: String?,
    val cover: String?,
    val current_location: String?,
    val description: String?,
    val emails: String?,
    val engagement: String?,
    val fan_count: Int?,
    val followers_count: Int?,
    val founded: String?,
    val has_whatsapp_business_number: Boolean?,
    val has_whatsapp_number: Boolean?,
    val link: String?,
    val location: String?,
    val name: String?,
    val phone: String?,
    val picture: String?,
    val rating_count: Int?,
    val username: String?,
    val website: String?
)