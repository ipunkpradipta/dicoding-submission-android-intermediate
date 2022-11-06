package com.ipunkpradipta.submissionstoryapp.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ipunkpradipta.submissionstoryapp.database.StoriesDatabase
import com.ipunkpradipta.submissionstoryapp.network.ApiService
import com.ipunkpradipta.submissionstoryapp.network.StoriesResponse
import com.ipunkpradipta.submissionstoryapp.network.StoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoriesRemoteMediatorTest{
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoriesDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoriesDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoriesRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, StoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService(private val token: String) : ApiService {
    override suspend fun getStories(
        token: String,
        page: Int,
        size: Int,
        location: Int
    ): Response<StoriesResponse> {
        val items: MutableList<StoryItem> = arrayListOf()

        for (i in 0..100) {
            val quote = StoryItem(
                i.toString(),
                "name + $i",
                "quote $i",
                "https://story-api.dicoding.dev/images/stories/photos-1667632506834_HZYugpC3.jpg",
                "2022-11-05T07:15:06.835Z",
                3.5429957,
                98.6584
            )
            items.add(quote)
        }
        var responseApi:StoriesResponse = StoriesResponse(error = false, message = "ssss", listStory = items)
        return responseApi
    }
}