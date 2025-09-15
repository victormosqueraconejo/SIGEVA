package com.victor.sigeva

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
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

        var btnBack = findViewById<ImageView>(R.id.btnArrowBack)
        btnBack.setOnClickListener {
            finish()
        }

        idEleccionExtra = intent.getIntExtra("idEleccion",0)


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
        val url = "https://sigevaback-real.onrender.com/api/candidatos/listar/$idEleccionExtra"
        val client = Volley.newRequestQueue(this)

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val gson = Gson()
                    val data = gson.fromJson(response, CandidatoAPIListar::class.java)

                    if (!data.data.isNullOrEmpty()) {
                        lista.clear()
                        data.data.forEach { candidato ->
                            lista.add(
                                Candidato(
                                    idcandidatos = candidato.idcandidatos,
                                    nombres = "${candidato.aprendiz.nombres} ${candidato.aprendiz.apellidos}",
                                    numeroTarjeton = candidato.numeroTarjeton,
                                    propuesta = candidato.propuesta,
                                    centroFormacion = candidato.aprendiz.centro_formacion.centroFormacioncol
                                )
                            )
                        }
                        adapterCandidatos.ActulizarAdapter(lista)
                    } else {
                        Toast.makeText(this, "No hay candidatos disponibles para esta elección.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parseando candidatos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error al cargar candidatos: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        client.add(request)
    }

}
