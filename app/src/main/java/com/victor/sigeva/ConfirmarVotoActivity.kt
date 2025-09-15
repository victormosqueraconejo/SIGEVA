package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.flagging.Flags
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
    lateinit var idEleccionExtra : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_voto)

        btnConfirmarVoto = findViewById(R.id.btnConfirmarVoto)

        idEleccionExtra = intent.getStringExtra("idEleccion")!!

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

        var url = "https://sigevaback-0rj7.onrender.com/api/votoXCandidato/crear"

        fun formatearVotoStringToInt(idEleccion : String) : Int{
            return when (idEleccion) {
                "001" -> 1
                "002" -> 2
                "003" -> 3
                "004" -> 4
                else -> 0
            }
        }

        var datosPost = JSONObject()
        datosPost.put("idaprendiz", idUsuario)
        datosPost.put("idcandidatos", idCandidato)
        datosPost.put("ideleccion", formatearVotoStringToInt(idEleccionExtra))
        datosPost.put("contador", 1)

        var client = Volley.newRequestQueue(this)
        var request = object : JsonObjectRequest(Request.Method.POST, url, datosPost, {
            response -> // TODO: Respuesta correcta o respuesta incorrecta
            var i = Intent(this, VotoExitosoActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)

        }, { error ->
            Log.d("API VOto", error.toString())
            Toast.makeText(this, "Error al cargar el voto.", Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }

        }
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