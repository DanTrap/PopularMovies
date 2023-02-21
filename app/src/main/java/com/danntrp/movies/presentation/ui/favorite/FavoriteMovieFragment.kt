package com.danntrp.movies.presentation.ui.favorite

import android.app.Activity
import android.app.SearchManager
import android.app.SearchableInfo
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.danntrp.movies.R
import com.danntrp.movies.databinding.FragmentFavoriteMovieBinding
import com.danntrp.movies.presentation.adapters.MarginItemDecorator
import com.danntrp.movies.presentation.adapters.MovieAdapter
import com.danntrp.movies.presentation.ui.MovieSearch
import com.danntrp.movies.presentation.ui.ToolbarHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment(R.layout.fragment_favorite_movie), MenuProvider,
    MovieSearch {

    private lateinit var binding: FragmentFavoriteMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchableInfo: SearchableInfo
    private lateinit var menuHost: MenuHost
    private lateinit var toolbarHost: ToolbarHost
    private val favoriteMovieViewModel: FavoriteViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchableInfo = searchManager.getSearchableInfo((context as Activity).componentName)
        menuHost = context as MenuHost
        toolbarHost = context as ToolbarHost
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteMovieBinding.bind(view)

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)

        toolbarHost.setToolbar(binding.toolBar)

        movieAdapter = MovieAdapter()

        binding.favoriteMoviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(MarginItemDecorator(resources.getDimensionPixelSize(R.dimen.recycler_view_items_margin)))
        }

        favoriteMovieViewModel.favoriteMovies().observe(viewLifecycleOwner) {
            movieAdapter.differ.submitList(it)
        }
    }

    override fun searchMovieByName(query: String?) {
        if (query != null) {
            favoriteMovieViewModel.favoriteMoviesByName(query).observe(viewLifecycleOwner) {
                movieAdapter.differ.submitList(it)
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.app_menu, menu)

        (menu.findItem(R.id.searchButton).actionView as SearchView).apply {
            setSearchableInfo(searchableInfo)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String?): Boolean {
                    searchMovieByName(query)
                    return true
                }

                override fun onQueryTextSubmit(query: String?) = true
            })
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem) = true

}