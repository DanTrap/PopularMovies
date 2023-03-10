package com.danntrp.movies.ui.fragments.popular

import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.filter
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.network.ConnectivityObserver
import com.danntrp.movies.domain.usecase.FavoriteMovieUseCase
import com.danntrp.movies.domain.usecase.PopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val popularMovieUseCase: PopularMovieUseCase,
    private val favoriteMovieUseCase: FavoriteMovieUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val getNetworkStatusStream = connectivityObserver.observe()

    private val query = MutableLiveData("")

    val filteredMovies = Transformations.switchMap(query) { query ->
        val movies = popularMovieUseCase.getPagedMovie().cachedIn(viewModelScope).asLiveData()
        if (query.isNullOrBlank()) {
            movies
        } else {
            movies.map { pagingData ->
                pagingData.filter {
                    it.name.lowercase().contains(Regex("\\b${query}*"))
                }
            }
        }
    }

    fun setQuery(query: String) {
        if (connectivityObserver.isConnected()) this.query.value = query
    }

    fun saveMovie(movie: Movie) = viewModelScope.launch {
        favoriteMovieUseCase.insert(movie)
    }
}