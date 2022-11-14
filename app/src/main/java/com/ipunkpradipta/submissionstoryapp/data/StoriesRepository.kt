package com.ipunkpradipta.submissionstoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.GsonBuilder
import com.ipunkpradipta.submissionstoryapp.data.local.room.StoriesDatabase
import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiService
import com.ipunkpradipta.submissionstoryapp.data.remote.response.StoryItem
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException

class StoriesRepository(private val storiesDatabase: StoriesDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token:String):LiveData<PagingData<StoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService,token),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStories()
            }
        ).liveData
    }

    fun getStoriesForMaps():LiveData<List<StoryItem>> = storiesDatabase.storiesDao().getAllStoriesForMaps()

    fun postStories(request: PostStoriesRequest) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage("Bearer ${request.token}",request.file,request.desc,request.lat,request.lon)
            emit(Result.Success(response))
            Log.d("StoriesRepository", "result: $response ")
        }catch (throwable: Throwable){
            Log.d("StoriesRepository", "throwable: $throwable ")
            when (throwable) {
                is IOException -> Result.Error("NetworkError")
                is HttpException -> {
                    try {
                        val gson = GsonBuilder().create()
                        try{
                            val response: DefaultResponse = gson.fromJson(
                                throwable.response()?.errorBody()?.string(),
                                DefaultResponse::class.java
                            )
                            if(response.error){
                                emit(Result.Error(response.message))
                            }else{
                                emit(Result.Success(response))
                            }
                            Log.d("AuthRepository", "response: $response} ")
                        }catch (e: IOException){
                            Log.d("AuthRepository", "exceptionGson: ${e.message.toString()} ")
                            emit(Result.Error("gsonException: ${e.message.toString()}"))

                        }
                    } catch (e: JSONException) {
                        emit(Result.Error(e.printStackTrace().toString()))
                        e.printStackTrace()
                    }
                }
                else -> {
                    emit(Result.Error("Call API Network Error"))
                }
            }
        }
    }

}