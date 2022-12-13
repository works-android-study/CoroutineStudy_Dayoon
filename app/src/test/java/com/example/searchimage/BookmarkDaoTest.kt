package com.example.searchimage

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.searchimage.db.AppDatabase
import com.example.searchimage.db.BookmarkDao
import com.example.searchimage.model.entity.Bookmark
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class BookmarkDaoTest {
    companion object {
        val bookmark1 = Bookmark("bookmark1", "sample link1")
        val bookmark2 = Bookmark("bookmark2", "sample link2")
    }
    @get:Rule
    var rule: TestRule = TestDispatcherRule()

    private lateinit var bookmarkDao: BookmarkDao
    private lateinit var appDb: AppDatabase

    @Before
    fun create() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDb = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()

        bookmarkDao = appDb.bookmarkDao()
    }

    @After
    fun cleanup() {
        appDb.close()
    }

    @Test
    fun addBookmarkTest() = runTest {
        bookmarkDao.insert(bookmark1)
        val result = bookmarkDao.getBookmarkList().first()
        assertEquals(listOf(bookmark1), result)
    }

    @Test
    fun deleteBookmarkTest() = runTest {
        bookmarkDao.insert(bookmark1)
        bookmarkDao.insert(bookmark2)
        bookmarkDao.delete(bookmark1)
        val result = bookmarkDao.getBookmarkList().first()
        assertEquals(listOf(bookmark2), result)
    }

}
