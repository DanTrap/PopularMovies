package com.danntrp.movies.presentation.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.danntrp.movies.R
import com.danntrp.movies.databinding.FragmentFavoriteMovieBinding
import com.danntrp.movies.presentation.adapters.MarginItemDecorator
import com.danntrp.movies.presentation.adapters.MovieAdapter
import com.danntrp.movies.presentation.ui.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment(R.layout.fragment_favorite_movie) {

    private lateinit var binding: FragmentFavoriteMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var navigation: Navigation
    private val favoriteMovieViewModel: FavoriteViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as Navigation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteMovieBinding.bind(view)
        binding.progressBar.visibility = View.GONE
        setupRecyclerView()

        favoriteMovieViewModel.favMovie.observe(viewLifecycleOwner) {
            movieAdapter.differ.submitList(it)
        }

        binding.popularButton.setOnClickListener {
            navigation.showPopularFragment()
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