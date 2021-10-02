package com.skilbox.bnetapi.plugins

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.skilbox.bnetapi.plugins.NetworkStatus.ANY
import com.skilbox.bnetapi.plugins.NetworkStatus.WIFI
import com.skilbox.bnetapi.plugins.NetworkStatus.refreshDisplay
import com.skilbox.bnetapi.plugins.NetworkStatus.sPref

class NetworkLissener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo
        if (WIFI == sPref && networkInfo?.type == ConnectivityManager.TYPE_WIFI) {

            refreshDisplay = true
        } else refreshDisplay = ANY == sPref && networkInfo != null
    }
}
