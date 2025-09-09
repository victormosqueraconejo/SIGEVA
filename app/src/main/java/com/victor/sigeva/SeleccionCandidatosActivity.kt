package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class SeleccionCandidatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_candidatos)

        findViewById<RecyclerView>(R.id.recyclerViewSeleccionCandidato).setOnClickListener {
            startActivity(Intent(this, ConfirmarVotoActivity::class.java))
        }
        findViewById<ImageView>(R.id.imgSineeva2).setOnClickListener {
            startActivity(Intent(this, ConfirmarVotoActivity::class.java))
        }
    }
}