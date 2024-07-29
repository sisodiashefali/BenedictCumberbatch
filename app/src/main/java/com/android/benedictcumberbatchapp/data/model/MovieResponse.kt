package com.android.benedictcumberbatchapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Result>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("status_code") val statusCode : Int?,
    @SerializedName("status_message") val message : String ,
    @SerializedName("success") val success : Boolean?,
)