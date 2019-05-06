package com.example.mundim.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mundim.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.fragment_patient_data2.view.*
import android.graphics.Bitmap
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import com.example.mundim.Services.showToast
import java.io.File
import java.io.FileOutputStream


class PictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mundim.R.layout.activity_picture)
        progressBar2.visibility = View.VISIBLE

        backBtn.setOnClickListener {
            onBackPressed()
        }
        downloadBtn.setOnClickListener {
            val draw = sampleImage.getDrawable() as BitmapDrawable
            val bitmap = draw.bitmap

            var outStream: FileOutputStream? = null
            val dir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "/HuB")
            dir.mkdirs()
            val fileName = String.format("IMG_%d.jpg", System.currentTimeMillis())
            val outFile = File(dir, fileName)
            outStream = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream!!.flush()
            outStream!!.close()


            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(outFile)
            intent.data = contentUri
            sendBroadcast(intent)
            showToast("Salvo com sucesso!")
        }
        Picasso.get().load("http://68.183.133.221/mundim/uploads/" +
                intent.extras.getString("url")).noFade().into(sampleImage, object : Callback {
            override fun onError(e: Exception) {}
            override fun onSuccess() {
                sampleImage.setAlpha(0f)
                sampleImage.animate().setDuration(500).alpha(1f).start()
                progressBar2.visibility = View.GONE
            }

        })
    }
}
