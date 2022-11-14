package com.ipunkpradipta.submissionstoryapp.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    var error: Boolean,
    var message: String,
    var listStory: List<StoryItem>
)

@Entity(tableName = "stories")
data class StoryItem(
    @PrimaryKey
    @field:SerializedName("id")
    var id : String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("description")
    var description:String,

    @field:SerializedName("photoUrl")
    var photoUrl:String,

    @field:SerializedName("createdAt")
    var createdAt:String,

    @field:SerializedName("lat")
    var lat : Double,

    @field:SerializedName("lon")
    var lon : Double
)