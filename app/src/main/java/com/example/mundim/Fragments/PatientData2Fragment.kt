package com.example.mundim.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_patient_data2.*
import kotlinx.android.synthetic.main.fragment_patient_data2.view.*
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.android.volley.Response
import com.example.mundim.R
import com.example.mundim.Services.execute
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.example.mundim.Services.showToast

class PatientData2Fargment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var currentPhotoPath: String = ""
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity!!,
                        "com.example.mundim",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            activity!!.sendBroadcast(mediaScanIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        galleryAddPic()
        showToast("Termo de consentimento adicionado")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_patient_data2, container, false)
        view.idTextView.setText(activity!!.intent.extras.getString("id"))
        view.idadeTextView.setText(activity!!.intent.extras.getString("idade"))
        view.sexoTextView.setText(activity!!.intent.extras.getString("sexo"))
        view.nomeTextView.setText(activity!!.intent.extras.getString("nome"))
        view.procedenciaTextView.setText(activity!!.intent.extras.getString("procedencia"))
        view.naturalidadeTextView.setText(activity!!.intent.extras.getString("naturalidade"))
        view.estadoTextView.setText(activity!!.intent.extras.getString("estado"))
        view.termosBtn.setOnClickListener {
            dispatchTakePictureIntent()
        }

        view.sendBtn.setOnClickListener {
            var intent = Intent()

            val nome = nomeTextView.text.toString()
            val id = idTextView.text.toString()
            val sexo = sexoTextView.text.toString()
            val idade = idadeTextView.text.toString()
            val procedencia = procedenciaTextView.text.toString()
            val naturalidade = naturalidadeTextView.text.toString()
            val estado = estadoTextView.text.toString()
            val db_id = activity!!.intent.extras.getString("db_id")

            intent.putExtra("nome", nome)
            intent.putExtra("id", id)
            intent.putExtra("sexo", sexo)
            intent.putExtra("idade", idade)
            intent.putExtra("procedencia", procedencia)
            intent.putExtra("naturalidade", naturalidade)
            intent.putExtra("estado", estado)
            intent.putExtra("position", activity!!.intent.extras.getString("position"))

            val input = """
                    UPDATE patients SET
                    nome = "$nome",
                    id = "$id",
                    sexo = "$sexo",
                    idade = "$idade",
                    procedencia = "$procedencia",
                    naturalidade = "$naturalidade",
                    estado = "$estado"
                    WHERE db_id = $db_id
                """.trimIndent()

            execute(input, context!!, Response.Listener {response ->  })
            activity!!.setResult(1, intent)
            activity!!.finish()
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {}

}
