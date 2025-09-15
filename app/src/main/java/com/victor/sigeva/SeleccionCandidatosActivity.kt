package com.victor.sigeva

import android.os.Bundle
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

    // Lista inicial
    var lista = mutableListOf<Candidato>()
    var idEleccionExtra : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_candidatos)

        // Recuperar el id de la elección
        idEleccionExtra = intent.getIntExtra("idEleccion", 0)

        recyclerViewCandidatos = findViewById(R.id.recyclerViewSeleccionCandidato)
        recyclerViewCandidatos.layoutManager = LinearLayoutManager(this)

        adapterCandidatos = AdapterSelccionCandidatos(lista) { candidato ->
            val bottomSheet = MasInformacionCandidatoFragment.newInstance(candidato)
            bottomSheet.show(supportFragmentManager, "Mas Informacion")
        }

        recyclerViewCandidatos.adapter = adapterCandidatos

        GetCandidatos()
    }

    fun GetCandidatos() {
        val url = "https://sigevaback-0rj7.onrender.com/api/candidatos/listar/$idEleccionExtra"
        val client = Volley.newRequestQueue(this)

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val gson = Gson()
                val data = gson.fromJson(response, CandidatoAPI::class.java)


                if (data != null && data.data != null && data.data.isNotEmpty()) {
                    lista.clear()
                    lista = data.data as MutableList<Candidato>
                    adapterCandidatos.ActulizarAdapter(lista)
                } else {
                    Toast.makeText(this, "No hay candidatos disponibles para esta elección.", Toast.LENGTH_SHORT).show()
                }

            }, { error ->
                Toast.makeText(this, "Error al cargar candidatos: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        client.add(request)
    }
}
