package com.ipunkpradipta.submissionstoryapp.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.ipunkpradipta.submissionstoryapp.MainDispatcherRule
import com.ipunkpradipta.submissionstoryapp.StoriesDummy
import com.ipunkpradipta.submissionstoryapp.adapter.StoriesPagerAdapter
import com.ipunkpradipta.submissionstoryapp.data.AuthPreferences
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.getOrAwaitValue
import com.ipunkpradipta.submissionstoryapp.network.StoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var storiesViewModel: StoriesViewModel

    @Before
    fun setUp() {
        storiesViewModel = StoriesViewModel(storiesRepository)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val dummyStories = StoriesDummy.generateDummyStoriesEntity()
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyStories)
        val expectedQuote = MutableLiveData<PagingData<StoryItem>>()

        expectedQuote.value = data
        Mockito.`when`(storiesRepository.getStories("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTJ2ZzVUMkFEb1NGQ1dZUF8iLCJpYXQiOjE2Njc2MzAzNTJ9.8SjH5C5ih3nUYzmHSJmhPVnwpeySYEcXmgIRhm6WgBg")).thenReturn(expectedQuote)

        val storiesViewModel = StoriesViewModel(storiesRepository)
        val actualQuote: PagingData<StoryItem> = storiesViewModel.getStories("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTJ2ZzVUMkFEb1NGQ1dZUF8iLCJpYXQiOjE2Njc2MzAzNTJ9.8SjH5C5ih3nUYzmHSJmhPVnwpeySYEcXmgIRhm6WgBg").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesPagerAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories, differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0].id, differ.snapshot()[0]?.id)

    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItem>>>() {
    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}