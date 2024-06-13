package com.example.share_a_file.listeners

import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager

object WiFiConnectionInfoListener: WifiP2pManager.ConnectionInfoListener {

    var status = ""

    override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
        if (info != null) {
            val inetAddress = info.groupOwnerAddress
            if (info.groupFormed && info.isGroupOwner){
                status = "Host"
            }else if(info.groupFormed){
                status = "Client"
            }
        }
    }
}