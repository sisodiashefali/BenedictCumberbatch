package com.android.benedictcumberbatchapp.data.datasource

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.benedictcumberbatchapp.data.model.MovieResponse
import com.android.benedictcumberbatchapp.data.model.Result
import com.android.benedictcumberbatchapp.data.network.ApiService
import com.android.benedictcumberbatchapp.utils.Constant
import com.android.benedictcumberbatchapp.utils.NetworkUtils
import com.google.gson.Gson
import retrofit2.Response
import java.lang.ref.WeakReference

class MoviePaging(
    private val apiService: ApiService,
    private val gson: Gson,
    context: Context
) : PagingSource<Int, Result>() {

    private val contextRef = WeakReference(context)

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val context = contextRef.get()
        if (context == null || !NetworkUtils.isInternetAvailable(context)) {
            return LoadResult.Error(NoInternetException())
        }

        val page = params.key ?: 1
        return try {
            val data = apiService.getAllMovies(page, Constant.API_KEY,Constant.PEOPLE_ID)
            if(!data.isSuccessful){
                val errorResponse = parseError(data)
                return LoadResult.Error(Exception(errorResponse?.message))
            }

            LoadResult.Page(
                data = data.body()?.results!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.body()?.results?.isEmpty()!!) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    class NoInternetException : Throwable("No internet connection")

    private fun parseError(response: Response<*>): MovieResponse? {
        val errorBody = response.errorBody()?.string()
        return gson.fromJson(errorBody, MovieResponse::class.java)
    }
}