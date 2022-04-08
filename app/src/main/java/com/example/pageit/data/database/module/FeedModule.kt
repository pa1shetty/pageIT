package com.example.pageit.data.database.module

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "feed", foreignKeys = [ForeignKey(
        entity = PageModule::class,
        parentColumns = arrayOf("page_id"),
        childColumns = arrayOf("pageId"),
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class FeedModule(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val story: String?,
    val message: String?,
    var createdTime: String,
    var pageId: String,
)

