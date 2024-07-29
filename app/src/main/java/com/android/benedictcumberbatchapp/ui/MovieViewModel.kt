package com.android.benedictcumberbatchapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.benedictcumberbatchapp.data.datasource.MoviePaging
import com.android.benedictcumberbatchapp.data.model.Result
import com.android.benedictcumberbatchapp.data.network.ApiService
import com.android.benedictcumberbatchapp.utils.UiState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.*


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val context: Context
): ViewModel() {


    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> get() = _result

    fun setMovieDetails(result: Result) {
        _result.value = result
    }

    private val _moviePagingData = MutableStateFlow<UiState<PagingData<Result>>>(UiState.Loading)
    val moviePagingList: StateFlow<UiState<PagingData<Result>>> = _moviePagingData

    init {
        collectPagingData()
    }


    private val examplePagingDataFlow: Flow<PagingData<Result>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MoviePaging(apiService, gson,context) }
    ).flow
        .cachedIn(viewModelScope)



    private fun collectPagingData() {
        viewModelScope.launch {
            examplePagingDataFlow
                .onStart { _moviePagingData.value = UiState.Loading }
                .collectLatest { pagingData -> _moviePagingData.value = UiState.Success(pagingData) }
        }
    }

}