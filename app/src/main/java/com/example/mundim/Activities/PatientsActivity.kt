package com.example.mundim.Activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import kotlinx.android.synthetic.main.activity_patients.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.android.volley.Response
import kotlinx.android.synthetic.main.patient_ticket.view.*
import org.jetbrains.anko.toast
import com.beust.klaxon.Klaxon
import com.example.mundim.Classes.Primary.Patient
import com.example.mundim.R
import com.example.mundim.Services.query
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.jetbrains.anko.async
import com.example.mundim.Services.GET_EDITED_PATIENT_DATA
import com.example.mundim.Services.GET_NEW_PATIENT_DATA

class PatientTicketAdapter: BaseAdapter {
    var patients = ArrayList<Patient>()
    var context: Activity? = null
    constructor(context: Activity, patients: ArrayList<Patient>):super(){
        this.patients = patients
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val patient = patients[position]
        var inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var MyView = inflator.inflate(R.layout.patient_ticket, null)
        MyView.nomeTextView.text = patient.nome
        MyView.dataTextView.text = patient.data

        MyView.setOnClickListener {
            val intent = Intent(context, PatientActivity::class.java)
            intent.putExtra("db_id", patient.db_id)
            intent.putExtra("id", patient.id)
            intent.putExtra("idade", patient.idade)
            intent.putExtra("sexo", patient.sexo)
            intent.putExtra("nome", patient.nome)
            intent.putExtra("procedencia", patient.procedencia)
            intent.putExtra("naturalidade", patient.naturalidade)
            intent.putExtra("estado", patient.estado)
            intent.putExtra("position", position.toString())
            startActivityForResult(context!!, intent, GET_EDITED_PATIENT_DATA,null)
        }
        return MyView
    }

    override fun getItem(position: Int): Any {
        return patients[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int{
        return patients.size
    }
}

class PatientsActivity : AppCompatActivity(){
    var patients = ArrayList<Patient>()
    private lateinit var patientsAdapter: PatientTicketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients)

        newPatientButton.setOnClickListener {
            val intent = Intent(this, NewPatientActivity::class.java)
            startActivityForResult(intent, GET_NEW_PATIENT_DATA )
        }

        patientsAdapter = PatientTicketAdapter(this, patients)
        patientsListView.adapter = patientsAdapter
        val listener = Response.Listener<String> { response ->
            loadingTextView.text = "Carregando pacientes..."
            Log.e("HttpClient", "success! response: $response")
            for (item in Klaxon().parseArray<Patient>(response)!!.iterator()){
                patients.add(item)
            }
            patientsAdapter.notifyDataSetChanged()
            loadingTextView.text = ""
        }
        async{
            query("SELECT * FROM patients", this@PatientsActivity, listener)
        }

        requestMultiplePermissions()

    }

    fun add(patient: Patient){
        patients.add(patient)
        patientsAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_NEW_PATIENT_DATA) {
            if (resultCode == 1) {
                add(
                    Patient(
                        db_id = data!!.extras.getString("db_id"),
                        id = data!!.extras.getString("id"),
                        idade = data!!.extras.getString("idade"),
                        sexo = data!!.extras.getString("sexo"),
                        nome = data!!.extras.getString("nome"),
                        procedencia = data!!.extras.getString("procedencia"),
                        naturalidade = data!!.extras.getString("naturalidade"),
                        estado = data!!.extras.getString("estado"),
                        data = data!!.extras.getString("data")
                    )
                )
            }
        }
        if (requestCode == GET_EDITED_PATIENT_DATA) {
            if (resultCode == 1) {
                var position = data!!.extras.getString("position").toInt()
                patients[position].id = data!!.extras.getString("id")
                patients[position].idade = data!!.extras.getString("idade")
                patients[position].sexo = data!!.extras.getString("sexo")
                patients[position].nome = data!!.extras.getString("nome")
                patients[position].procedencia = data!!.extras.getString("procedencia")
                patients[position].naturalidade = data!!.extras.getString("naturalidade")
                patients[position].estado = data!!.extras.getString("estado")
            }
        }
    }

    private fun requestMultiplePermissions() {
        Dexter.withActivity(this)
            .withPermissions(

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Precisamos dessas permissões!", Toast.LENGTH_SHORT).show()
                requestMultiplePermissions()
            }
            .onSameThread()
            .check()
    }

}
