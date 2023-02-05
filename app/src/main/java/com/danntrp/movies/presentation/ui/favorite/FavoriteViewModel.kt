package com.danntrp.movies.presentation.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.usecase.FavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteMovieUseCase: FavoriteMovieUseCase
) : ViewModel() {

    private val _favMovie = MutableLiveData<List<Movie>>()
    val favMovie: LiveData<List<Movie>> = _favMovie

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() = viewModelScope.launch {
        _favMovie.postValue(favoriteMovieUseCase.getMovies())
    }

    fun deleteMovie(id: Int) = viewModelScope.launch {
       favoriteMovieUseCase.delete(id)
    }
}