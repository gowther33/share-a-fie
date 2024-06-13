package com.example.share_a_file.network

import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class WiFiDirectServer(

):Thread() {
    val serverSocket :ServerSocket

    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    var msg:String? = null
    var socket:Socket? = null

    init {
        serverSocket = ServerSocket(8888)
    }

    override fun run() {
        super.run()
        try {
            socket = serverSocket.accept()
            inputStream = socket?.getInputStream()!!
            outputStream = socket?.getOutputStream()!!
        }catch (e: Exception){
            e.printStackTrace()
        }

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            val buffer = ByteArray(1024)
            var bytes:Int
            while (socket!=null){
                try {
                    bytes = inputStream.read(buffer)
                    if(bytes > 0){
                        val finalBytes = bytes
                        handler.post(
                            Runnable {
                                val tempMsg = String(buffer, 0, finalBytes)
                                msg = tempMsg
                            }
                        )
                    }
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
        }

    }
}