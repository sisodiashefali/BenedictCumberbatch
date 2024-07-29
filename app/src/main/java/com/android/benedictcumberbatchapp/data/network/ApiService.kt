package com.android.benedictcumberbatchapp.data.network

import com.android.benedictcumberbatchapp.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie")
    suspend fun getAllMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String,
        @Query("with_people") peopleID: Int
    ): Response<MovieResponse>
}