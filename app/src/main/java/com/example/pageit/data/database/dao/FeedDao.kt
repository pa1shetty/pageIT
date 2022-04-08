package com.example.pageit.data.database.dao

import androidx.room.*
import com.example.pageit.data.database.module.FeedModule
import kotlinx.coroutines.flow.Flow

@Dao
@Entity
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFeeds(feed: List<FeedModule>)


    @Query("SELECT * FROM feed where pageId=(:pageId)")
    @JvmSuppressWildcards
    fun getFeeds(pageId: String): Flow<List<FeedModule>>

    @Query("DELETE  FROM feed where pageId=(:pageId)")
    @JvmSuppressWildcards
    fun deleteFeedsOfAPage(pageId: String)


}