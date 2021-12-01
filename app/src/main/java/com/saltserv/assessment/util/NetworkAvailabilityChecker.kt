package com.saltserv.assessment.util

import android.content.Context
import android.net.ConnectivityManager

interface NetworkAvailabilityChecker {
    val isConnectedToInternet: Boolean
}

internal class NetworkAvailabilityCheckerImpl(context: Context) : NetworkAvailabilityChecker {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isConnectedToInternet: Boolean
        get() {

            val activeNetwork = cm.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnected
        }
}