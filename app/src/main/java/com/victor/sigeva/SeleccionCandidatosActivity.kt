package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SeleccionCandidatosActivity : AppCompatActivity() {
    lateinit var recyclerViewCandidatos: RecyclerView


    // TODO: Esta es la lista que se debe modificar en la peticion a la API
    var lista = mutableListOf<Candidato>(
        Candidato(null, "Carlos Mendoza", "001", "Gestion Empresarial", "Fomentar el emprendimiento a través de alianzas estratégicas con el sector productivo."),
        Candidato(null, "Alex Chaguendo", "002", "Analisis y Desaroollo de Software", "Fomentar el emprendimiento a través de alianzas estratégicas con el sector productivo."),
        Candidato(null, "Santiago Valencia", "003", "Construccion", "Fomentar el emprendimiento a través de alianzas estratégicas con el sector productivo.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_candidatos)

        recyclerViewCandidatos = findViewById(R.id.recyclerViewSeleccionCandidato)
        recyclerViewCandidatos.layoutManager = LinearLayoutManager(this)
        var adapterCandidatos = AdapterSelccionCandidatos(lista) {
            var intent = Intent(this, ConfirmarVotoActivity::class.java)
            startActivity(intent)
        }

        recyclerViewCandidatos.adapter = adapterCandidatos



    }
}