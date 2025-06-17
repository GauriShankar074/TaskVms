package com.gauri.taskvms.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConnectivityManager {

    // Expose network connectivity state as a StateFlow
    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun setConnected(connected: Boolean) {
        _isConnected.value = connected
    }
}