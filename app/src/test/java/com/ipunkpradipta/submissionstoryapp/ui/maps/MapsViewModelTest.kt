package com.ipunkpradipta.submissionstoryapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ipunkpradipta.submissionstoryapp.StoriesDummy
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.data.remote.response.StoryItem
import com.ipunkpradipta.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dummyStories = StoriesDummy.generateDummyStoriesEntity()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var mapsViewModel: MapsViewModel

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storiesRepository)
    }

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `whenGetStoriesReturnShouldNotNull`() = run{
        val expected = MutableLiveData<List<StoryItem>>()
        expected.value = dummyStories
        `when`(storiesRepository.getStoriesForMaps()).thenReturn(expected)

        val actualStories = mapsViewModel.getAllStories().getOrAwaitValue()

        Mockito.verify(storiesRepository).getStoriesForMaps()
        Assert.assertNotNull(actualStories)
    }
}