package com.example.share_a_file

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.provider.Settings
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.share_a_file.adapter.PeersRecyclerViewAdapter
import com.example.share_a_file.network.listeners.WiFiConnectionInfoListener
import com.example.share_a_file.network.manager.DownloadManager
import com.example.share_a_file.receiver.WiFiDirectBroadcastReceiver
import com.example.test.databinding.ActivityMainLayoutBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding:ActivityMainLayoutBinding
    private var permissionsGranted = false

    // Permissions launcher
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted){
            permissionsGranted = true
            Toast.makeText(applicationContext,"Permission granted", Toast.LENGTH_SHORT)
        }else{
            Toast.makeText(applicationContext,"Permission denied", Toast.LENGTH_SHORT)
        }
    }

    lateinit var dm : DownloadManager

    lateinit var receiver:WiFiDirectBroadcastReceiver
    private val intentFilter = IntentFilter()
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var manager: WifiP2pManager

    var isWifiP2pEnabled:Boolean = false

    private val peers = mutableListOf<WifiP2pDevice>()
    val deviceNameList = ArrayList<PeersModel>()
    val deviceList = ArrayList<WifiP2pDevice>()

    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:PeersRecyclerViewAdapter

    // Peers listener
    val peerListListener =  object: WifiP2pManager.PeerListListener{

        override fun onPeersAvailable(wiFiP2pDeviceList: WifiP2pDeviceList?) {
            if(wiFiP2pDeviceList?.equals(peers)?.not()!!){

                if (wiFiP2pDeviceList.deviceList.size == 0){
                    binding.tvStatus.text = "No Device found"
                    return
                }

                peers.clear()
                peers.addAll(wiFiP2pDeviceList.deviceList)

                wiFiP2pDeviceList.deviceList.forEach{device->
                    deviceNameList.add(PeersModel(device.deviceName))
                    deviceList.add(device)
                }

                setUpRecyclerView()
            }
        }
    }

    // Connection Info Listener
    val connectionInfoListener = WiFiConnectionInfoListener

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.rvPeers

        if (checkPermissions().not()){
            permissionLauncher.launch(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        initializeIntentFilters()

        setUpOnClickListeners()

    }

    private fun setUpOnClickListeners() {

        // On Off wifi listener
        binding.btnOnOff.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        }

        // Discover peers
        binding.btnDiscover.setOnClickListener {
            manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {

                @SuppressLint("SetTextI18n")
                override fun onSuccess() {
                    // Code for when the discovery initiation is successful goes here.
                    // No services have actually been discovered yet, so this method
                    // can often be left blank. Code for peer discovery goes in the
                    // onReceive method, detailed below.
                    binding.tvStatus.text = "Discovering..."
                }

                override fun onFailure(reasonCode: Int) {
                    // Code for when the discovery initiation fails goes here.
                    // Alert the user that something went wrong.
                    binding.tvStatus.text = "Discovery failed"
                }
            })
        }
    }

    private fun initializeIntentFilters() {
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        receiver = WiFiDirectBroadcastReceiver(manager, channel, this)

        // Indicates a change in the Wi-Fi Direct status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)

        // Indicates the state of Wi-Fi Direct connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
    }

    private fun setUpRecyclerView() {
        adapter = PeersRecyclerViewAdapter(deviceNameList){model->
            deviceList.forEach { device ->
                val config = WifiP2pConfig()
                config.deviceAddress = device.deviceAddress
                manager.connect(
                    channel,
                    config,
                    object : WifiP2pManager.ActionListener {
                        @SuppressLint("SetTextI18n")
                        override fun onSuccess() {
                            val type = WiFiConnectionInfoListener.status
                            if (type == "Client"){
                                binding.tvStatus.text = "Connected: ${device.deviceName}"
                            }else{
                                binding.tvStatus.text = type
                            }

                        }

                        override fun onFailure(reason: Int) {
                            binding.tvStatus.text = "Not Connected"
                        }
                    })
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private fun checkPermissions():Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED
        }else{
            true
        }
    }
}