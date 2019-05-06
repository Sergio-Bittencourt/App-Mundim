package com.example.mundim.Classes.Primary
import android.app.PendingIntent.getActivity
import android.support.v7.app.AppCompatActivity
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.view.View

class Patient{
    var id:String? = null
    var idade:String? = null
    var sexo:String? = null
    var nome:String? = null
    var procedencia:String? = null
    var naturalidade:String? = null
    var estado:String? = null
    var data:String? = null
    var db_id:String? = null
    constructor(id:String="",
                idade:String="",
                sexo:String="",
                nome:String="",
                procedencia:String="",
                naturalidade:String="",
                estado:String="",
                data:String="",
                db_id:String=""
                ){
        this.id = id
        this.data = data
        this.idade = idade
        this.sexo = sexo
        this.nome = nome
        this.procedencia = procedencia
        this.naturalidade = naturalidade
        this.estado = estado
        this.db_id = db_id
    }
}
