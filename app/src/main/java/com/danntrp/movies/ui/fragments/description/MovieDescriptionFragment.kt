package com.danntrp.movies.ui.fragments.description

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.danntrp.movies.R
import com.danntrp.movies.databinding.FragmentMovieDescriptionBinding
import com.danntrp.movies.ui.activity.ToolbarHost
import com.danntrp.movies.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDescriptionFragment : Fragment(R.layout.fragment_movie_description), MenuProvider {

    private lateinit var binding: FragmentMovieDescriptionBinding
    private lateinit var menuHost: MenuHost
    private lateinit var toolbarHost: ToolbarHost
    private val descriptionViewModel: DescriptionViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuHost = context as MenuHost
        toolbarHost = context as ToolbarHost
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDescriptionBinding.bind(view)

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)

        toolbarHost.setToolbar(binding.toolBar)

        descriptionViewModel.getMovieDescription(arguments?.getInt("movie-id") ?: 0)

        descriptionViewModel.movieDescription.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    Log.d("ABOBA", "DESC FRAGMENT SUCCESS")
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { movie ->
                        binding.apply {
                            Glide.with(binding.root).load(movie.posterUrl).into(posterImageView)
                            collapsing.title = movie.name
                            descriptionTextView.text = movie.description
                            genresTextView.text = String.format(
                                this.root.resources.getString(R.string.genres),
                                movie.genres.joinToString()
                            )
                            countriesTextView.text = String.format(
                                this.root.resources.getString(R.string.countries),
                                movie.countries.joinToString()
                            )
                        }
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

        binding.repeatButton.setOnClickListener {

        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.app_menu, menu)
        menu.findItem(R.id.searchButton).isVisible = false
    }

    override fun onMenuItemSelected(menuItem: MenuItem) = false
}