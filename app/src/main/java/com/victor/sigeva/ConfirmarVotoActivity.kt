package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import org.json.JSONObject

class ConfirmarVotoActivity : AppCompatActivity() {

    lateinit var et1 : EditText
    lateinit var et2 : EditText
    lateinit var et3 : EditText
    lateinit var et4 : EditText
    lateinit var et5 : EditText
    lateinit var et6 : EditText

    lateinit var btnConfirmarVoto : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_voto)

        btnConfirmarVoto = findViewById(R.id.btnConfirmarVoto)

        et1 = findViewById(R.id.et1)
        et2 = findViewById(R.id.et2)
        et3 = findViewById(R.id.et3)
        et4 = findViewById(R.id.et4)
        et5 = findViewById(R.id.et5)
        et6 = findViewById(R.id.et6)

        // TODO: Validar Codigo antes de enviar al backend

        btnConfirmarVoto.setOnClickListener {
            EnviarVoto(LoginActivity.aprendiz.id,intent.getIntExtra("idCandidato", 0)) // Todo: Cambir el default value
        }





        setupOtpInputs()



    }


    fun EnviarVoto(idUsuario : Int, idCandidato : Int) {

        var url = ""

        var datosPost = JSONObject()
        datosPost.put("idUsuario", idUsuario)
        datosPost.put("idCandidato", idCandidato)

        var client = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(Request.Method.POST, url, datosPost, {
            response -> // TODO: Respuesta correcta o respuesta incorrecta

        }, { error ->
            Toast.makeText(this, "Error al cargar el voto.", Toast.LENGTH_SHORT).show()
        })
        client.add(request)
    }


    fun setupOtpInputs() {
        val editTexts = listOf(et1, et2, et3, et4, et5, et6)

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}