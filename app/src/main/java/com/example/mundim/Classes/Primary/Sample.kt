package com.example.mundim.Classes.Primary
import android.app.PendingIntent.getActivity
import android.support.v7.app.AppCompatActivity
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.view.View

class Sample{
    var id:String? = null
    var patient_id:String? = null
    var area_anatomica:String? = null
    var antecedentes_familiares:String? = null
    var diagnostico_dermatologista:String? = null
    var diagnostico_biopsia:String? = null
    var url:String? = null
    var derma_url:String? = null
    var nome:String? = null
    var data:String? = null
    constructor(id:String="",
                patient_id:String="",
                area_anatomica:String="",
                antecedentes_familiares:String="",
                diagnostico_dermatologista:String="",
                diagnostico_biopsia:String="",
                url:String="",
                nome:String="",
                data:String="",
                derma_url:String?=""
    ){
        this.id = id
        this.patient_id = patient_id
        this.area_anatomica = area_anatomica
        this.antecedentes_familiares = antecedentes_familiares
        this.diagnostico_dermatologista = diagnostico_dermatologista
        this.diagnostico_biopsia = diagnostico_biopsia
        this.url = url
        this.nome = nome
        this.data = data
        this.derma_url = derma_url
    }
}
