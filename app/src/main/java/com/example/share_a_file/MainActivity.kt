package com.example.share_a_file

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.share_a_file.databinding.ActivityMainBinding
import com.example.share_a_file.network.ConnectionStateListener
import com.example.share_a_file.network.manager.DownloadManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var dm : DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val imgView = findViewById<ImageView>(R.id.imageView)
        val button = findViewById<MaterialButton>(R.id.btn_download)
        dm = DownloadManager(this, imgView)


        button.setOnClickListener {
            ConnectionStateListener.getConnectionState(this)
            if (ConnectionStateListener.isWifiConn){
                val url = "https://images.unsplash.com/photo-1570051008600-b34baa49e751?q=80&w=2085&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                dm.onPreExecute()

                CoroutineScope(Dispatchers.IO).launch {
                    val bm = dm.doInBackground(url)
                    withContext(Dispatchers.Main){
                        dm.onPostExecute(bm)
                    }
                }
            }
            else{
                Toast.makeText(this, "No Wifi", Toast.LENGTH_LONG).show()
            }

        }
    }
}