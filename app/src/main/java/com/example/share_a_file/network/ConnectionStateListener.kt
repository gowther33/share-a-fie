package com.example.share_a_file.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

object ConnectionStateListener {

    private const val DEBUG_TAG = "NetworkStatusExample"

    private var connMgr: ConnectivityManager? = null
    var isWifiConn: Boolean = false
    var isMobileConn: Boolean = false

    fun getConnectionState(context:Context){
        if (connMgr==null){
            connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
        connMgr?.allNetworks?.forEach { network ->
            connMgr?.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }

        Log.d(DEBUG_TAG, "Wifi connected: $isWifiConn")
        Log.d(DEBUG_TAG, "Mobile connected: $isMobileConn")
    }
}