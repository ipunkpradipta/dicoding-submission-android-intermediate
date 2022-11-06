package com.ipunkpradipta.submissionstoryapp

import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest
import com.ipunkpradipta.submissionstoryapp.network.StoryItem

object StoriesDummy {
    fun generateDummyStoriesEntity():List<StoryItem>{
        val storiesList = ArrayList<StoryItem>()

        for (i in 0..10){
            val story = StoryItem(
                i.toString(),
                "name$i",
                "desc $i",
                "https://story-api.dicoding.dev/images/stories/photos-1667632506834_HZYugpC3.jpg",
                "2022-11-05T07:15:06.835Z",
                3.5429957,
                98.6584
            )
            storiesList.add(story)
        }
        return storiesList
    }
}