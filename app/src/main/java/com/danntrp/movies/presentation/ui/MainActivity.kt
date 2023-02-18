package com.danntrp.movies.presentation.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.danntrp.movies.R
import com.danntrp.movies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

        navController = navHost.findNavController()

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.popularMovieFragment, R.id.favoriteMovieFragment))

        binding.bottomNavView.setupWithNavController(navController)

        binding.toolBar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDescriptionFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEARCH) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            (binding.toolBar.menu.findItem(R.id.searchButton).actionView as SearchView).apply {
                setQuery(query, true)
                clearFocus()
            }
        }
    }
}