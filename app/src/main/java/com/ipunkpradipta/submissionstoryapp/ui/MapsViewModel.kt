package com.ipunkpradipta.submissionstoryapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.network.StoryItem

class MapsViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    fun getAllStories() : LiveData<List<StoryItem>> {
        return storiesRepository.getStoriesForMaps()
    }

}