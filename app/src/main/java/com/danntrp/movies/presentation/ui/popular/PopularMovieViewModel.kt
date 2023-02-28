package com.danntrp.movies.presentation.ui.popular

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.usecase.FavoriteMovieUseCase
import com.danntrp.movies.domain.usecase.PopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val popularMovieUseCase: PopularMovieUseCase,
    private val favoriteMovieUseCase: FavoriteMovieUseCase
) : ViewModel() {

    private val _pagedData = MutableLiveData<PagingData<Movie>>()
    val pagedData: LiveData<PagingData<Movie>> = _pagedData

    init {
        getPopularMoviesPaged()
    }

    private fun getPopularMoviesPaged() = viewModelScope.launch {
        popularMovieUseCase.getPagedMovie().cachedIn(viewModelScope).collectLatest {
            _pagedData.postValue(it)
        }
    }

    fun saveMovie(movie: Movie) = viewModelScope.launch {
        favoriteMovieUseCase.insert(movie)
    }
}