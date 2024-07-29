package com.android.benedictcumberbatchapp.utils

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object NoInternet : UiState<Nothing>()
}
