package com.example.pageit.data.database

import com.example.pageit.data.database.dao.FeedDao
import com.example.pageit.data.database.dao.PageDao
import com.example.pageit.data.database.module.FeedModule
import com.example.pageit.data.database.module.PageModule
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val pageDao: PageDao,
    private val feedDao: FeedDao
) {
    fun savePages(pages: List<PageModule>) {
        pageDao.savePages(pages)
    }

    fun getPages() = pageDao.getPages()
    fun getFirstPage() = pageDao.getFirstPage()
    fun getPageDetails(id: String) = pageDao.getPageDetails(id)
    fun saveFeeds(feeds: List<FeedModule>, pageId: String) {
        feedDao.deleteFeedsOfAPage(pageId)
        feedDao.saveFeeds(feeds)
    }

    fun getFeeds(pageId: String) = feedDao.getFeeds(pageId)

}