package com.example.share_a_file.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.example.share_a_file.MainActivity

class WiFiDirectBroadcastReceiver(
    val manager: WifiP2pManager,
    val channel:WifiP2pManager.Channel,
    private val activity:MainActivity
):BroadcastReceiver() {

    private val TAG = "receiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wi-Fi Direct mode is enabled or not, alert the Activity.
//                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
//                activity.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()

                manager.requestPeers(channel, activity.peerListListener)
                Log.d(TAG, "P2P peers changed")
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Connection state changed! We should probably do something about that.
                val networkInfo = intent.getParcelableExtra<NetworkInfo>(
                    WifiP2pManager.EXTRA_NETWORK_INFO)
                if (networkInfo?.isConnected!!){
                    manager.requestConnectionInfo(channel, activity.connectionInfoListener)
                }else{
                    activity.connectionInfoListener.status = "Not Connected"
                }

            }
//            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
//                (activity.supportFragmentManager.findFragmentById(R.id.frag_list) as DeviceListFragment)
//                    .apply {
//                        updateThisDevice(
//                            intent.getParcelableExtra(
//                                WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice
//                        )
//                    }
//            }
        }

    }
}