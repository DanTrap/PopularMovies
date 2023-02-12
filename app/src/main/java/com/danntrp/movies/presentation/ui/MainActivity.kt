package com.danntrp.movies.presentation.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.danntrp.movies.R
import com.danntrp.movies.databinding.ActivityMainBinding
import com.danntrp.movies.presentation.ui.description.MovieDescriptionFragment
import com.danntrp.movies.presentation.ui.favorite.FavoriteMovieFragment
import com.danntrp.movies.presentation.ui.navigation.Navigation
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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val popFragment = supportFragmentManager.findFragmentByTag("pop-fragment")
                val favFragment = supportFragmentManager.findFragmentByTag("fav-fragment")
                if (popFragment != null && popFragment.isVisible) {
                    finish()
                } else if (favFragment != null && favFragment.isVisible) {
                    showPopularFragment()
                } else {
                    pop()
                }
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, popularFragment, "pop-fragment")
                .commit()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, favoriteFragment, "fav-fragment")
                .addToBackStack(null)
                .hide(favoriteFragment)
                .commit()
        }
    }

    override fun showFavoriteFragment() {
        supportFragmentManager
            .beginTransaction()
            .hide(popularFragment)
            .show(favoriteFragment)
            .commit()
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