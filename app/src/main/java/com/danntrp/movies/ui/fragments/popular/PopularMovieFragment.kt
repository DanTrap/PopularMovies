package com.danntrp.movies.ui.fragments.popular

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
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danntrp.movies.R
import com.danntrp.movies.databinding.FragmentPopularMovieBinding
import com.danntrp.movies.domain.network.ConnectivityObserver
import com.danntrp.movies.ui.activity.ToolbarHost
import com.danntrp.movies.ui.adapters.LoadingStateAdapter
import com.danntrp.movies.ui.adapters.MarginItemDecorator
import com.danntrp.movies.ui.adapters.MoviePagerAdapter
import com.danntrp.movies.ui.fragments.MovieSearch
import com.danntrp.movies.ui.fragments.description.MovieDescriptionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie), MenuProvider, MovieSearch {

    private lateinit var binding: FragmentPopularMovieBinding
    private lateinit var movieAdapter: MoviePagerAdapter
    private lateinit var searchableInfo: SearchableInfo
    private lateinit var menuHost: MenuHost
    private lateinit var toolbarHost: ToolbarHost
    private val movieViewModel: PopularMovieViewModel by viewModels()

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

        movieAdapter = MoviePagerAdapter(
            onItemClick = { id ->
                findNavController().navigate(
                    R.id.action_popularMovieFragment_to_movieDescriptionFragment,
                    bundleOf(MovieDescriptionFragment.MOVIE_ID_KEY to id)
                )
            },
            onItemLongClick = { movie ->
                movieViewModel.saveMovie(movie)
            }
        ).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.popularMoviesRecyclerView.apply {
            adapter = movieAdapter.withLoadStateFooter(LoadingStateAdapter())
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MarginItemDecorator(resources.getDimensionPixelSize(R.dimen.recycler_view_items_margin)))
        }

        movieViewModel.filteredMovies.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                movieAdapter.submitData(it)
            }
        }

        movieAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) {
            when {
                it.refresh is LoadState.Error -> {
                    binding.networkLayout.visibility = View.VISIBLE
                }
                it.refresh is LoadState.NotLoading && it.append is LoadState.NotLoading -> {
                    binding.networkLayout.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.getNetworkStatusStream.collect {
                if (it == ConnectivityObserver.NetworkStatus.AVAILABLE) {
                    movieAdapter.retry()
                }
            }
        }

        val reselectedTabLiveData = findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>(R.id.popular.toString())

        reselectedTabLiveData?.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.appBar.setExpanded(true, true)
            binding.popularMoviesRecyclerView.smoothScrollToPosition(0)
            reselectedTabLiveData.value = null
        }
    }

    override fun searchMovieByName(query: String?) {
        if (query != null) movieViewModel.setQuery(query)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.app_menu, menu)

        (menu.findItem(R.id.searchButton).actionView as SearchView).apply {
            setSearchableInfo(searchableInfo)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                var job: Job? = null

                override fun onQueryTextChange(query: String?): Boolean {
                    job?.cancel()
                    job = lifecycleScope.launch {
                        delay(500)
                        searchMovieByName(query)
                    }
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