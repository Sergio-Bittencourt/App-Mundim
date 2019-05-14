package com.example.mundim.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Response
import com.example.mundim.R
import com.example.mundim.Services.GET_DERMA_IMAGE
import com.example.mundim.Services.execute
import com.example.mundim.Services.showToast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sample.*
import android.app.Activity
import android.provider.MediaStore
import com.example.mundim.Classes.Auxiliary.ImageUploader

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        backBtn.setOnClickListener {
            onBackPressed()
        }
        sendBtn3.setOnClickListener {
            onBackPressed()
        }
        microscopeBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_DERMA_IMAGE)
        }
        trashBtn.setOnClickListener {
            intent.putExtra("deleted", 1)
            intent.putExtra("position", intent.extras.getString("position"))
            val id = intent.extras.getString("id")
            val input = """
                    DELETE FROM samples
                    WHERE id = $id
                """.trimIndent()
            execute(input, this, Response.Listener { response ->  })
            setResult(1, intent)
            finish()
        }
        Picasso.get().load("http://68.183.133.221/mundim/uploads/" +
                intent.extras.getString("url")).noFade().into(sampleImage, object : Callback {
            override fun onError(e: Exception) {
                showToast("Sem fotos salvas da amostra.")
                progressBarSampleImage.visibility = View.GONE
            }
            override fun onSuccess() {
                sampleImage.setAlpha(0f)
                sampleImage.animate().setDuration(500).alpha(1f).start()
                progressBarSampleImage.visibility = View.GONE
                labelCamera.visibility = View.VISIBLE
            }

        })
        Picasso.get().load("http://68.183.133.221/mundim/uploads/" +
                intent.extras.getString("derma_url")).noFade().into(dermaImage, object : Callback {
            override fun onError(e: Exception) {
                progressBarDerma.visibility = View.GONE
                dermaImage.visibility = View.GONE
                labelDerma.visibility = View.GONE
            }
            override fun onSuccess() {
                sampleImage.setAlpha(0f)
                sampleImage.animate().setDuration(500).alpha(1f).start()
                progressBarDerma.visibility = View.GONE
                labelDerma.visibility = View.VISIBLE
                dermaImage.visibility = View.VISIBLE
            }

        })
        val bundle:Bundle = intent.extras
        areaAnatomicaTextView.setText(bundle.getString("area_anatomica"))
        antecedentesFamiliaresTextView.setText(bundle.getString("antecedentes_familiares"))
        diagnosticoBTextView.setText(bundle.getString("diagnostico_biopsia"))
        diadnosticoDTextView.setText(bundle.getString("diagnostico_dermatologista"))
        sampleImage.setOnClickListener {
            val intt = Intent(this, PictureActivity::class.java)
            intt.putExtra("url", intent.extras.getString("url"))
            startActivity(intt)
        }
        dermaImage.setOnClickListener {
            val intt = Intent(this, PictureActivity::class.java)
            intt.putExtra("url", intent.extras.getString("derma_url"))
            startActivity(intt)
        }

        sendBtn3.setOnClickListener {
            val id = intent.extras.getString("id")
            val area_anatomica = areaAnatomicaTextView.text.toString()
            val antecedentes_familiares = antecedentesFamiliaresTextView.text.toString()
            val diagnostico_dermatologista = diadnosticoDTextView.text.toString()
            val diagnostico_biopsia = diagnosticoBTextView.text.toString()

            intent.putExtra("area_anatomica", area_anatomica)
            intent.putExtra("antecedentes_familiares", antecedentes_familiares)
            intent.putExtra("diagnostico_dermatologista", diagnostico_dermatologista)
            intent.putExtra("diagnostico_biopsia", diagnostico_biopsia)
            intent.putExtra("deleted", 0)
            intent.putExtra("position", intent.extras.getString("position"))

            val input = """
                    UPDATE samples SET
                    area_anatomica = "$area_anatomica",
                    antecedentes_familiares = "$antecedentes_familiares",
                    diagnostico_dermatologista = "$diagnostico_dermatologista",
                    diagnostico_biopsia = "$diagnostico_biopsia"
                    WHERE id = $id
                """.trimIndent()

            execute(input, this, Response.Listener { response ->  })
            setResult(1, intent)
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == GET_DERMA_IMAGE) {
            var uri = data?.data
            var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri);
            val derma_url = "_DERMA_" + System.currentTimeMillis().toString() + ".png"
            ImageUploader().uploadImage(bitmap, this, derma_url, 50)
            val id = intent.extras.getString("id")
            val input = """
                UPDATE samples SET
                derma_url = "$derma_url"
                WHERE id = $id
            """.trimIndent()
            execute(input, this, Response.Listener { response ->  })
            intent.putExtra("derma_url", derma_url)

            Picasso.get().load("http://68.183.133.221/mundim/uploads/" +
                    derma_url).noFade().into(dermaImage, object : Callback {
                override fun onError(e: Exception) {
                    progressBarDerma.visibility = View.GONE
                    dermaImage.visibility = View.GONE
                    labelDerma.visibility = View.GONE
                }
                override fun onSuccess() {
                    sampleImage.setAlpha(0f)
                    sampleImage.animate().setDuration(500).alpha(1f).start()
                    progressBarDerma.visibility = View.GONE
                    labelDerma.visibility = View.VISIBLE
                    dermaImage.visibility = View.VISIBLE
                }

            })
        }
    }
}
