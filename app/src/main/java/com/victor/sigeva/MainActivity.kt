package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var recyclerViewVotaciones : RecyclerView
    var lista = mutableListOf<Votacion>(
        Votacion(null,"Cauca","Centro de Servicios y Gestion Empresarial", "Diurna" ),
        Votacion(null,"Antioquia","Centro de Teleinformatica y Produccion Industrial", "Noctura" ),
        Votacion(null,"Cauca","Centro de Servicios y Gestion Empresarial", "Diurna" )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        recyclerViewVotaciones = findViewById(R.id.recyclerViewVotacionesActivas)
        recyclerViewVotaciones.layoutManager = LinearLayoutManager(this)
        var adapterVotacion = AdapterVotacionesActivas(lista) { votacion ->
            // TODO: Hacer el intent para llevar a la siguiente pagina y enviar el la votacion
            var  intent  = Intent(this, SeleccionCandidatosActivity::class.java)
            startActivity(intent)
        }
        recyclerViewVotaciones.adapter = adapterVotacion

    }
}