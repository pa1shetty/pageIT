package com.example.pageit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pageit.data.database.dao.FeedDao
import com.example.pageit.data.database.dao.PageDao
import com.example.pageit.data.database.module.FeedModule
import com.example.pageit.data.database.module.PageModule


@Database(
    entities = [PageModule::class, FeedModule::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPageDao(): PageDao
    abstract fun getFeedDao(): FeedDao


}