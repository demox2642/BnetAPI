package com.skilbox.bnetapi.plugins

object NetworkStatus {

    const val WIFI = "Wi-Fi"
    const val ANY = "Any"

    var wifiConnected = false

    var mobileConnected = false

    var refreshDisplay = true

    var sPref: String? = null
}
