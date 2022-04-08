package com.example.pageit.data.database.dao

import androidx.room.*
import com.example.pageit.data.database.module.PageModule
import kotlinx.coroutines.flow.Flow

@Dao
@Entity
interface PageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePages(pages: List<PageModule>)

    @Query("SELECT * FROM page")
    @JvmSuppressWildcards
    fun getPages(): Flow<List<PageModule>>

    @Query("SELECT * FROM page limit 1")
    @JvmSuppressWildcards
    fun getFirstPage(): List<PageModule>


    @Query("DELETE FROM page")
    fun nukePages()

    @Query("SELECT * FROM page Where page_id in (:id)")
    fun getPageDetails(id: String): Flow<PageModule>

}