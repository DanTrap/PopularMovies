package com.danntrp.movies.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.danntrp.movies.data.remote.MovieService
import com.danntrp.movies.data.remote.model.MovieDto
import okio.IOException
import retrofit2.HttpException

class MoviePagingSource(
    private val movieService: MovieService
) : PagingSource<Int, MovieDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val pageIndex = params.key ?: FIRST_PAGE
            Log.d("ABOBA", "PAGE INDEX = $pageIndex")
            val response = movieService.getPopularMovies(page = pageIndex)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                LoadResult.Page(
                    data = body.films,
                    prevKey = if (pageIndex == FIRST_PAGE) null else pageIndex - 1,
                    nextKey = if (pageIndex == LAST_PAGE) null else pageIndex + 1
                )
            } else {
                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return null
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val LAST_PAGE = 20
    }
}