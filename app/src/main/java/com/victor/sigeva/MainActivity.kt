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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        findViewById<RecyclerView>(R.id.recyclerViewVotacionesActivas).setOnClickListener {
            startActivity(Intent(this, SeleccionCandidatosActivity::class.java))
        }
        findViewById<CardView>(R.id.cardNoVotaciones).setOnClickListener {
            startActivity(Intent(this, SeleccionCandidatosActivity::class.java))
        }
        startActivity(Intent(this, SeleccionCandidatosActivity::class.java))



        var listaGlobal  = mutableListOf<Usuario>(
            Usuario(1,"USuario 1", "Manana", "CTPI"),
            Usuario(2,"USuario 2", "Tarde", "Comercio"),
            Usuario(3,"USuario 3", "Manana", "CTPI"),
        )

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewVotacionesActivas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fun getUsuario(url : String) {

            var client = Volley.newRequestQueue(this)
            var request = StringRequest(Request.Method.GET, url, { response ->
                var gson = Gson()
                var data = gson.fromJson(response, Usuario::class.java)
                listaGlobal.add(data)

            }, { error ->
                Log.d("API", error.toString())
            })

            client.add(request)
        }



        getUsuario("www.api.com/usuario")

    }
}