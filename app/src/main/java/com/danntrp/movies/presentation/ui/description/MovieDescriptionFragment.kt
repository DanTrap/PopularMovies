package com.danntrp.movies.presentation.ui.description

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.danntrp.movies.R
import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.databinding.FragmentMovieDescriptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDescriptionFragment : Fragment(R.layout.fragment_movie_description) {

    private lateinit var binding: FragmentMovieDescriptionBinding
    private val descriptionViewModel: DescriptionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDescriptionBinding.bind(view)

        val movieId = arguments?.getInt("movie-id")
        if (savedInstanceState == null) descriptionViewModel.getMovieDescription(movieId!!)

        descriptionViewModel.movieDescription.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { movie ->
                        binding.apply {
                            Glide.with(binding.root).load(movie.posterUrl).into(posterImageView)
                            movieNameTextView.text = movie.name
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

        binding.toolBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.repeatButton.setOnClickListener {
            descriptionViewModel.getMovieDescription(movieId!!)
        }
    }
}