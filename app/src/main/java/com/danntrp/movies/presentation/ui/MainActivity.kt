package com.danntrp.movies.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danntrp.movies.R
import com.danntrp.movies.databinding.ActivityMainBinding
import com.danntrp.movies.presentation.ui.description.MovieDescriptionFragment
import com.danntrp.movies.presentation.ui.favorite.FavoriteMovieFragment
import com.danntrp.movies.presentation.ui.popular.PopularMovieFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigation {

    private lateinit var binding: ActivityMainBinding
    private val popularFragment = PopularMovieFragment()
    private val favoriteFragment = FavoriteMovieFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, popularFragment)
                .commit()
        }
    }

    override fun showFavoriteFragment() {
        if (favoriteFragment.isAdded) {
            supportFragmentManager
                .beginTransaction()
                .hide(popularFragment)
                .show(favoriteFragment)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragmentContainer, favoriteFragment)
                .hide(popularFragment)
                .commit()
        }
    }

    override fun showPopularFragment() {
        supportFragmentManager
            .beginTransaction()
            .hide(favoriteFragment)
            .show(popularFragment)
            .commit()
    }

    override fun showDescriptionFragment(id: Int) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, MovieDescriptionFragment.instance(id))
            .commit()
    }

    override fun pop() {
        supportFragmentManager.popBackStack()
    }
}

interface Navigation {
    fun showFavoriteFragment()
    fun showPopularFragment()
    fun showDescriptionFragment(id: Int)
    fun pop()
}
