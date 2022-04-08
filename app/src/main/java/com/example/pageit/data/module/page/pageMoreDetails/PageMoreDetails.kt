package com.example.pageit.data.module.page.pageMoreDetails

data class PageMoreDetails(
    val about: String?,
    val access_token: String?,
    val birthday: String?,
    val business: String?,
    val category: String?,
    val company_overview: String?,
    val contact_address: String?,
    val cover: Cover?,
    val current_location: String?,
    val description: String?,
    val emails: List<String>?,
    val engagement: Engagement?,
    val fan_count: Int?,
    val followers_count: Int?,
    val founded: String?,

    val has_whatsapp_business_number: Boolean?,
    val has_whatsapp_number: Boolean?,
    val id: String,
    val link: String?,
    val location: Location?,
    val name: String?,
    val phone: String?,
    val picture: Picture?,
    val rating_count: Int?,
    val username: String?,
    val website: String?
)