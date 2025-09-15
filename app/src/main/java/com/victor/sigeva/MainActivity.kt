package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    var lista = mutableListOf<Votacion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        recyclerViewVotaciones = findViewById(R.id.recyclerViewVotacionesActivas)
        recyclerViewVotaciones.layoutManager = LinearLayoutManager(this)
        var adapterVotacion = AdapterVotacionesActivas(lista) { votacion ->
            // TODO: Hacer el intent para llevar a la siguiente pagina y enviar el la votacion

            var  intent  = Intent(this, SeleccionCandidatosActivity::class.java)
            intent.putExtra("idEleccion", votacion.ideleccion)
            startActivity(intent)
        }
        recyclerViewVotaciones.adapter = adapterVotacion
        GetVotacion() { votacions ->
            adapterVotacion.ActulizarVotacion(votacions)
        }
    }

    fun GetVotacion(actualizarVotacion: (List<Votacion>) -> Unit) {
        var url = "https://sigevaback-0rj7.onrender.com/api/eleccion/activas"
        var client = Volley.newRequestQueue(this)

        var request = StringRequest(Request.Method.GET, url , { response ->
            val gson = Gson()
            var data = gson.fromJson(response, VotacionesAPI::class.java)
            actualizarVotacion(data.eleccionesActivas)

        }, { error ->
            Toast.makeText(this, "Error: ${error.message.toString()}", Toast.LENGTH_SHORT).show()
        })

        client.add(request)

    }

}