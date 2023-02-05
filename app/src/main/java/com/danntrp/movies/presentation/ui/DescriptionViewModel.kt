package com.danntrp.movies.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.domain.model.MovieDescription
import com.danntrp.movies.domain.usecase.MovieDescriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(
    private val movieDescriptionUseCase: MovieDescriptionUseCase
) : ViewModel() {

    private val _movieDescription = MutableLiveData<Resource<MovieDescription>>()
    val movieDescription: LiveData<Resource<MovieDescription>> = _movieDescription

    fun getMovieDescription(id: Int) = viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
        _movieDescription.postValue(when (throwable) {
            is IOException -> Resource.Error("Network")
            is HttpException -> Resource.Error("Server")
            else -> Resource.Error("Something")
        })
    }) {
        _movieDescription.postValue(Resource.Loading())
        _movieDescription.postValue(movieDescriptionUseCase.getDescription(id))
    }
}