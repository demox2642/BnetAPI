package com.skilbox.bnetapi

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skilbox.bnetapi.database.Database
import com.skilbox.bnetapi.plugins.NetworkLissener

class MainActivity : AppCompatActivity() {
    private lateinit var receiver: NetworkLissener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Database.init(context = this)

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        receiver = NetworkLissener()
        this.registerReceiver(receiver, filter)
    }

    public override fun onDestroy() {
        super.onDestroy()

        this.unregisterReceiver(receiver)
    }
}
