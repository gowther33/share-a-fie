package com.example.share_a_file

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.share_a_file.network.manager.DownloadManager
import com.example.share_a_file.ui.theme.Share_a_fileTheme

class MainActivity : ComponentActivity() {

    lateinit var dm : DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Share_a_fileTheme{
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {innerPaddings->
                    HomeScreen(
                        Modifier.padding(innerPaddings)
                    )

                }
            }
        }

//        button.setOnClickListener {
//            ConnectionStateListener.getConnectionState(this)
//            if (ConnectionStateListener.isWifiConn){
//                val url = "https://images.unsplash.com/photo-1570051008600-b34baa49e751?q=80&w=2085&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
//                dm.onPreExecute()
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    val bm = dm.doInBackground(url)
//                    withContext(Dispatchers.Main){
//                        dm.onPostExecute(bm)
//                    }
//                }
//            }
//            else{
//                Toast.makeText(this, "No Wifi", Toast.LENGTH_LONG).show()
//            }
//        }
    }

}

@Composable
fun HomeScreen(modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
         Button(
                onClick = {
                    
                }) {
                Text(text = "Connect")
         }
        Button(
                onClick = { /*TODO*/ }) {
                Text(text = "Select")
        }
        Button(
            onClick = { /*TODO*/ }) {
            Text(text = "Send")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview(){
    Share_a_fileTheme {
        HomeScreen(Modifier.fillMaxSize())
    }
}