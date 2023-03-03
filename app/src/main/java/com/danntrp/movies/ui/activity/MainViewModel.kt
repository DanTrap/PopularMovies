package com.danntrp.movies.ui.activity

import androidx.lifecycle.ViewModel
import com.danntrp.movies.domain.network.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject  constructor(
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val getNetworkStatusStream = connectivityObserver.observe()
}