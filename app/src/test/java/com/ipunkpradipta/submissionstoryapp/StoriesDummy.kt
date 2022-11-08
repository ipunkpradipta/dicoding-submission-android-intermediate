package com.ipunkpradipta.submissionstoryapp

import com.ipunkpradipta.submissionstoryapp.data.PostStoriesRequest
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest
import com.ipunkpradipta.submissionstoryapp.network.StoryItem
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

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

    fun generateDummyPostStories():PostStoriesRequest{
        val token = "token"
        val descRequestBody = "desc".toRequestBody("text/plain".toMediaType())
        val latRequestBody = "3.5429957".toRequestBody("text/plain".toMediaType())
        val lonRequestBody = "98.6584".toRequestBody("text/plain".toMediaType())
        val file = MultipartBody.Part.create("photo".toRequestBody())
        return PostStoriesRequest(token,descRequestBody,file,latRequestBody,lonRequestBody)
    }

    fun generateDummyResponsePostStories():DefaultResponse{
        return DefaultResponse(false,"Upload Success")
    }
}