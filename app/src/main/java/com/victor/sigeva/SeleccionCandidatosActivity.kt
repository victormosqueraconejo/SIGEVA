package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class SeleccionCandidatosActivity : AppCompatActivity() {
    lateinit var recyclerViewCandidatos: RecyclerView
    lateinit var adapterCandidatos : AdapterSelccionCandidatos


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
        adapterCandidatos = AdapterSelccionCandidatos(lista) { candidato ->
            var bottomSheet = MasInformacionCandidatoFragment.newInstance(candidato)
            bottomSheet.show(supportFragmentManager, "Mas Informacion")
        }

        recyclerViewCandidatos.adapter = adapterCandidatos

        GetCandidatos()

    }

    fun GetCandidatos() {
        var url = ""
        var client = Volley.newRequestQueue(this)
        var request = StringRequest(Request.Method.GET, url,
            { response ->
                var gson = Gson()
                var data = gson.fromJson(response, CandidatoAPI::class.java)
                lista.clear()
                lista = data.Candidatos as MutableList<Candidato>
                adapterCandidatos.ActulizarAdapter(lista)


            }, { error ->
                Toast.makeText(this, "Error : ${error.message.toString()}", Toast.LENGTH_SHORT).show()
            })

        client.add(request)
    }

}