package com.example.share_a_file.network

import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class WiFiDirectClient(
    val hostAdd:String,
):Thread() {
    private val socket: Socket
    private lateinit var inputStream:InputStream
    private lateinit var outputStream:OutputStream
    var msg:String? = null
    init {
        socket = Socket()
    }

    override fun run() {
        super.run()
        try {
            socket.connect(
                InetSocketAddress(
                    hostAdd,
                    8888
                ),
                500
            )
            inputStream = socket.getInputStream()
            outputStream = socket.getOutputStream()
        }catch (e:IOException){
            e.printStackTrace()
        }

        val executorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executorService.execute {
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