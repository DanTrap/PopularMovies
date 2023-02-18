package com.danntrp.movies.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danntrp.movies.domain.usecase.FavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteMovieUseCase: FavoriteMovieUseCase
) : ViewModel() {

    fun favoriteMovies() = favoriteMovieUseCase.favorites()

    fun deleteMovie(id: Int) = viewModelScope.launch {
        favoriteMovieUseCase.delete(id)
    }

    fun favoriteMoviesByName(name: String) = favoriteMovieUseCase.favoritesByName(name)
}