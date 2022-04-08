package com.example.pageit.di

import android.content.Context
import androidx.room.Room
import com.example.pageit.data.database.AppDatabase
import com.example.pageit.data.database.dao.FeedDao
import com.example.pageit.data.database.dao.PageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun providePageDao(appDatabase: AppDatabase): PageDao {
        return appDatabase.getPageDao()
    }

    @Provides
    fun provideFeedDao(appDatabase: AppDatabase): FeedDao {
        return appDatabase.getFeedDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pageIT_db"
        ).build()
    }


}