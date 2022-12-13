package com.example.searchimage

import com.example.searchimage.db.BookmarkLocalApi
import com.example.searchimage.model.entity.Bookmark
import com.example.searchimage.network.api.DownloadApiClient
import com.example.searchimage.network.api.SearchApiClient
import com.example.searchimage.ui.SearchImageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchImageViewModelTest {
    @get:Rule
    var rule: TestRule = TestDispatcherRule()

    lateinit var viewModel: SearchImageViewModel

    @Mock
    lateinit var searchApiClient: SearchApiClient
    @Mock lateinit var downloadApiClient: DownloadApiClient
    @Mock lateinit var bookmarkLocalApi: BookmarkLocalApi
    @Mock lateinit var bookmark: Bookmark

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = spy(SearchImageViewModel(
            searchApiClient, downloadApiClient, bookmarkLocalApi
        ))
    }

    @Test
    fun getBookmarkListTest() = runTest {
        val flow: Flow<List<Bookmark>> = flow {
            emit(listOf(bookmark))
        }
        `when`(bookmarkLocalApi.getBookmarkList()).thenReturn(flow)

        viewModel.initBookmarkList()
        val result = viewModel.bookmarkListFlow.first()
        assertEquals(listOf(bookmark), result)
    }

}
