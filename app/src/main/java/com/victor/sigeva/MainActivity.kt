package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

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

    }
}