package com.danntrp.movies.presentation.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.danntrp.movies.R
import com.danntrp.movies.databinding.ActivityMainBinding
import com.danntrp.movies.domain.network.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToolbarHost {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolBar: Toolbar
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment)
                .findNavController()

        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.popularMovieFragment, R.id.favoriteMovieFragment))

        binding.bottomNavView.setupWithNavController(navController)

        binding.bottomNavView.setOnItemReselectedListener {
            navController.currentBackStackEntry?.savedStateHandle?.set(it.itemId.toString(), "UP")
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDescriptionFragment -> binding.bottomNavView.visibility = View.GONE
                else -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            mainViewModel.getNetworkStatusStream.collect {
                when (it) {
                    ConnectivityObserver.NetworkStatus.AVAILABLE -> {
                        if (binding.networkStatusTextView.visibility == View.VISIBLE) {
                            animateNoInternetConnectionView(false)
                        }
                    }
                    ConnectivityObserver.NetworkStatus.LOST -> {
                        if (binding.networkStatusTextView.visibility != View.VISIBLE) {
                            animateNoInternetConnectionView(true)
                        }
                    }
                }
            }
        }
    }

    private fun animateNoInternetConnectionView(show: Boolean) {
        if (show) {
            binding.networkStatusTextView.apply {
                setBackgroundColor(getColor(R.color.grey))
                text = getString(R.string.no_connection)
                visibility = View.VISIBLE
            }
            ObjectAnimator.ofFloat(
                binding.networkStatusTextView,
                "translationY",
                binding.networkStatusTextView.height.toFloat(),
                0f
            ).apply {
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        } else {
            binding.networkStatusTextView.apply {
                ValueAnimator.ofArgb(getColor(R.color.grey), getColor(R.color.bright_green)).apply {
                    duration = 300
                    addUpdateListener {
                        setBackgroundColor(it.animatedValue as Int)
                    }
                }.start()
                text = getString(R.string.back_online)
            }
            ObjectAnimator.ofFloat(
                binding.networkStatusTextView,
                "translationY",
                0f,
                binding.networkStatusTextView.height.toFloat()
            ).apply {
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                startDelay = 2000
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.networkStatusTextView.visibility = View.GONE
                    }
                })
            }.start()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEARCH) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            (toolBar.menu.findItem(R.id.searchButton).actionView as SearchView).apply {
                setQuery(query, true)
            }
        }
    }

    override fun setToolbar(toolBar: Toolbar) {
        this.toolBar = toolBar
        setSupportActionBar(toolBar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}