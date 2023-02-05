package com.danntrp.movies.presentation.ui.popular

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
import com.danntrp.movies.presentation.ui.description.MovieDescriptionFragment
import com.danntrp.movies.presentation.ui.favorite.FavoriteMovieFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie) {

    private lateinit var binding: FragmentPopularMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPopularMovieBinding.bind(view)

        setupRecyclerView()

        movieAdapter.setOnItemClickListener {
            val fragment = MovieDescriptionFragment()
            fragment.arguments = Bundle().apply {
                putInt("movie-id", it)
            }
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("pop-to-description")
                .commit()
        }

        movieAdapter.setOnItemLongClickListener {
            Log.d("ABOBA", "LONG CLICK, $it")
            movieViewModel.saveMovie(it)
        }

        movieViewModel.popMovie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("ABOBA", "SUCCESS")
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
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, FavoriteMovieFragment())
                .addToBackStack("pop-to-fav")
                .commit()
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