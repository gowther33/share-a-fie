package com.example.share_a_file.network

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DownloadManager(private val context: Context, private val imageView: ImageView){

    private var progressDialog: ProgressDialog? = null

    fun onPreExecute() {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage("Downloading...")
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog?.show()
    }

    suspend fun doInBackground( imgUrl: String): Bitmap? {
        val url = URL(imgUrl)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.doInput = true
            connection.connect();
            val input = connection.inputStream;
            val mBitmap = BitmapFactory.decodeStream(input);
            return mBitmap;
        } catch (e: IOException) {
            // Log exception
            return null;
        } finally {
            connection.disconnect()
        }
    }

    fun onPostExecute(result: Bitmap?) {
        progressDialog?.dismiss()
        if (result != null) {
            imageView.setImageBitmap(result)
        }
    }
}