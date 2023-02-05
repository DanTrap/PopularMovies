package com.danntrp.movies.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.usecase.PopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val popularMovieUseCase: PopularMovieUseCase
) : ViewModel() {

    private val _popMovie = MutableLiveData<Resource<List<Movie>>>()
    val popMovie: LiveData<Resource<List<Movie>>> = _popMovie

    private var popularMoviePage = 1

    init {
        getPopularMovies()
    }

    fun getPopularMovies() = viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
        _popMovie.postValue(when (throwable) {
            is IOException -> Resource.Error("Network")
            is HttpException -> Resource.Error("Server")
            else -> Resource.Error("Something")
        })
    }) {
        _popMovie.postValue(Resource.Loading())
        _popMovie.postValue(popularMovieUseCase.getMovies(popularMoviePage))
    }
}