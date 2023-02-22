package com.danntrp.movies.presentation.ui.popular

import android.app.Activity
import android.app.SearchManager
import android.app.SearchableInfo
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danntrp.movies.R
import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.databinding.FragmentPopularMovieBinding
import com.danntrp.movies.presentation.adapters.MarginItemDecorator
import com.danntrp.movies.presentation.adapters.MovieAdapter
import com.danntrp.movies.presentation.ui.MovieSearch
import com.danntrp.movies.presentation.ui.ToolbarHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie), MenuProvider, MovieSearch {

    private lateinit var binding: FragmentPopularMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchableInfo: SearchableInfo
    private lateinit var menuHost: MenuHost
    private lateinit var toolbarHost: ToolbarHost
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchableInfo = searchManager.getSearchableInfo((context as Activity).componentName)
        menuHost = context as MenuHost
        toolbarHost = context as ToolbarHost
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPopularMovieBinding.bind(view)

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)

        toolbarHost.setToolbar(binding.toolBar)

        movieAdapter = MovieAdapter().apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.popularMoviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(MarginItemDecorator(resources.getDimensionPixelSize(R.dimen.recycler_view_items_margin)))
        }

        movieAdapter.setOnItemClickListener { id ->
            findNavController().navigate(
                R.id.action_popularMovieFragment_to_movieDescriptionFragment,
                bundleOf("movie-id" to id)
            )
        }

        movieAdapter.setOnItemLongClickListener {
            Log.d("ABOBA", "LONG CLICK, $it")
            movieViewModel.saveMovie(it)
        }

        movieViewModel.popMovie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.networkLayout.visibility = View.GONE
                    binding.progressBarRepeat.visibility = View.GONE
                    response.data?.let {
                        movieAdapter.differ.submitList(response.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.networkLayout.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        delay(1500)
                        withContext(Dispatchers.Main) {
                            binding.progressBarRepeat.visibility = View.GONE
                        }
                    }
                    response.message?.let {
                        Log.e("ABOBA", it)
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("ABOBA", "LOADING")
                }
            }
        }

        binding.repeatButton.setOnClickListener {
            binding.progressBarRepeat.visibility = View.VISIBLE
            movieViewModel.getPopularMovies()
        }

        val liveData = findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>(R.id.popular.toString())

        liveData?.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.appBar.setExpanded(true, true)
            binding.popularMoviesRecyclerView.smoothScrollToPosition(0)
            liveData.value = null
        }
    }

    override fun searchMovieByName(query: String?) {
        if (query != null) {
            //TODO
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

                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    return true
                }
            })
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem) = true
}