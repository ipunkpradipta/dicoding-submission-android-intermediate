package com.ipunkpradipta.submissionstoryapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ipunkpradipta.submissionstoryapp.data.StoriesRepository
import com.ipunkpradipta.submissionstoryapp.data.remote.response.StoryItem

class MapsViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    fun getAllStories() : LiveData<List<StoryItem>> {
        return storiesRepository.getStoriesForMaps()
    }

}