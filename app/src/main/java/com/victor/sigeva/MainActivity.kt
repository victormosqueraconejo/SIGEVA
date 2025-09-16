package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var recyclerViewVotaciones : RecyclerView
    lateinit var cardNoVotaciones : CardView
    var lista = mutableListOf<Votacion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        cardNoVotaciones = findViewById(R.id.cardNoVotaciones)

        recyclerViewVotaciones = findViewById(R.id.recyclerViewVotacionesActivas)
        recyclerViewVotaciones.layoutManager = LinearLayoutManager(this)

        // Al dar clic en una votacion
        val adapterVotacion = AdapterVotacionesActivas(lista) { votacion ->
            val intent = Intent(this, SeleccionCandidatosActivity::class.java)
            intent.putExtra("idEleccion", votacion.ideleccion)
            startActivity(intent)
        }

        recyclerViewVotaciones.adapter = adapterVotacion

        // CORRECCIÓN: Intentar cargar las votaciones, si falla, reintentar
        cargarVotaciones(adapterVotacion, 0)
    }

    private fun cargarVotaciones(adapterVotacion: AdapterVotacionesActivas, intentos: Int) {
        try {
            // Verificar que aprendiz esté disponible
            val centroFormacion = LoginActivity.aprendiz.CentroFormacion

            GetVotacion { votacions ->
                // si la lista está vacía o no
                if (votacions.isNotEmpty()) {
                    adapterVotacion.ActulizarVotacion(votacions)
                } else {
                    Toast.makeText(this, "No hay votaciones activas disponibles", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: UninitializedPropertyAccessException) {
            // Si aprendiz no está inicializado y no hemos intentado muchas veces
            if (intentos < 5) {
                Handler(Looper.getMainLooper()).postDelayed({
                    cargarVotaciones(adapterVotacion, intentos + 1)
                }, 200) // Esperar 200ms y reintentar
            } else {
                // Si después de varios intentos aún no está inicializado, regresar a login
                Toast.makeText(this, "Error de sesión. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show()
                regresarALogin()
            }
        }
    }

    fun GetVotacion(actualizarVotacion: (List<Votacion>) -> Unit) {
        val url = "https://sigevaback-real.onrender.com/api/eleccionPorCentro/${LoginActivity.aprendiz.CentroFormacion}"
        val client = Volley.newRequestQueue(this)

        val request = StringRequest(Request.Method.GET, url, { response ->
            val gson = Gson()
            val data = gson.fromJson(response, VotacionesAPI::class.java)

            // respuesta no sea nula
            if (data != null && data.eleccionesActivas != null) {
                actualizarVotacion(data.eleccionesActivas)
                cardNoVotaciones.isVisible = false
            } else {
                Toast.makeText(this, "No se pudieron obtener las votaciones.", Toast.LENGTH_SHORT).show()
                cardNoVotaciones.isVisible = true
            }

        }, { error ->
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            cardNoVotaciones.isVisible = true
        })

        client.add(request)
    }

    private fun regresarALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}