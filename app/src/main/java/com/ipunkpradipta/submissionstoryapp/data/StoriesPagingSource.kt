package com.ipunkpradipta.submissionstoryapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ipunkpradipta.submissionstoryapp.data.remote.retrofit.ApiService
import com.ipunkpradipta.submissionstoryapp.data.remote.response.StoryItem

class StoriesPagingSource(private val apiService: ApiService, private val token:String):PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        Log.d("StoriesPagingSource","LOAD in PagingSource")
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(token,position,params.loadSize,1)
            LoadResult.Page(
                data = response.body()!!.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.body()!!.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}