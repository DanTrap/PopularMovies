package com.danntrp.movies.domain.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>

    fun isConnected(): Boolean

    enum class NetworkStatus {
        AVAILABLE, LOST
    }
}