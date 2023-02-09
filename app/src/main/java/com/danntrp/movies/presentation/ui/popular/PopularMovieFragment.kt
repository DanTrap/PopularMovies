package com.danntrp.movies.presentation.ui.popular

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.danntrp.movies.R
import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.databinding.FragmentPopularMovieBinding
import com.danntrp.movies.presentation.adapters.MarginItemDecorator
import com.danntrp.movies.presentation.adapters.MovieAdapter
import com.danntrp.movies.presentation.ui.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie) {

    private lateinit var binding: FragmentPopularMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var navigation: Navigation
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as Navigation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPopularMovieBinding.bind(view)

        setupRecyclerView()

        movieAdapter.setOnItemClickListener { id ->
            navigation.showDescriptionFragment(id)
        }

        movieAdapter.setOnItemLongClickListener {
            Log.d("ABOBA", "LONG CLICK, $it")
            movieViewModel.saveMovie(it)
        }

        movieViewModel.popMovie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("ABOBA", "POP FRAGMENT SUCCESS")
                    response.data?.let {
                        movieAdapter.differ.submitList(response.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.networkLayout.visibility = View.VISIBLE
                    response.message?.let {
                        Log.e("ABOBA", it)
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.networkLayout.visibility = View.GONE
                    Log.d("ABOBA", "LOADING")
                }
            }
        }

        binding.popularButton.setOnClickListener {
            movieViewModel.getPopularMovies()
        }

        binding.favoriteButton.setOnClickListener {
            navigation.showFavoriteFragment()
        }

        binding.repeatButton.setOnClickListener {
            movieViewModel.getPopularMovies()
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        binding.popularMoviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(MarginItemDecorator(resources.getDimensionPixelSize(R.dimen.recycler_view_items_margin)))
        }
    }
}