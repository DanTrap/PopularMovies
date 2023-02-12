package com.danntrp.movies.presentation.ui.navigation

interface Navigation {
    fun showFavoriteFragment()
    fun showPopularFragment()
    fun showDescriptionFragment(id: Int)
    fun pop()
}