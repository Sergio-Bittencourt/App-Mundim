package com.example.mundim.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.example.mundim.Activities.NewPatientActivity
import com.example.mundim.Activities.NewSampleActivity
import com.example.mundim.Classes.Auxiliary.ImageUploader
import com.example.mundim.R
import com.example.mundim.Services.*
import kotlinx.android.synthetic.main.fragment_patient_data.*
import kotlinx.android.synthetic.main.fragment_patient_data.view.*
import kotlinx.android.synthetic.main.fragment_sample_data.*
import org.jetbrains.anko.async
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SampleDataFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_sample_data, container, false)

        view.sendBtn.setOnClickListener {
            val bundle = activity!!.intent.extras
            Log.e("ACTIVITY_RESULT", "-2")

            var patient_id = bundle.getString("db_id")
            var sample_id = System.currentTimeMillis().toString()
            val area_anatomica = areaAnatomicaTextView.text.toString()
            val antecedentes_familiares = antecedentesFamiliaresTextView.text.toString()
            val diagnostico_dermatologista = diadnosticoDTextView.text.toString()
            val diagnostico_biopsia = diagnosticoBTextView.text.toString()
            val data = SimpleDateFormat("dd/MM, HH:mm").format(Date())
            sample_id = sample_id + System.currentTimeMillis().toString() + ".png"

            var intent = Intent()
            intent.putExtra("area_anatomica", area_anatomica)
            intent.putExtra("antecedentes_familiares", antecedentes_familiares)
            intent.putExtra("diagnostico_dermatologista", diagnostico_dermatologista)
            intent.putExtra("diagnostico_biopsia", diagnostico_biopsia)
            intent.putExtra("diagnostico_biopsia", diagnostico_biopsia)
            intent.putExtra("data", data)
            intent.putExtra("nome", bundle.getString("nome"))
            intent.putExtra("patient_id", patient_id)
            intent.putExtra("url", sample_id)

            val listener = Response.Listener<String> { response ->
                Log.e("HttpClient", "Sample added at id: $response")
                intent.putExtra("id", response)
                activity!!.setResult(1, intent)
                Log.e("ACTIVITY_RESULT", "-1")
                activity!!.finish()
            }

            async {
                val input = """
                    INSERT INTO samples
                    (patient_id, area_anatomica, antecedentes_familiares, diagnostico_dermatologista, diagnostico_biopsia, url, data)
                    VALUES
                    ('$patient_id', '$area_anatomica', '$antecedentes_familiares', '$diagnostico_dermatologista', '$diagnostico_biopsia', '$sample_id', '$data')
                """.trimIndent()
                execute(input, context!!, listener)
            }

            var origin = File(activity?.getExternalFilesDir(null), PIC_FILE_NAME)
            var destination = File(activity?.getExternalFilesDir(null), "$sample_id")
            origin.copyTo(destination)
            val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, Uri.fromFile(destination)).rotate().crop()
            ImageUploader().uploadImage(bitmap, activity as NewSampleActivity, sample_id, 100)
        }

        return view
    }


}
