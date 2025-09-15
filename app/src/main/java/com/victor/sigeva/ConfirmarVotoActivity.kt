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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.flagging.Flags
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
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
            EnviarVoto(LoginActivity.aprendiz.id,intent.getIntExtra("idCandidato", 0), formatearVotoStringToInt(idEleccionExtra)) // Todo: Cambir el default value
        }



        setupOtpInputs()



    }
    fun formatearVotoStringToInt(idEleccion : String) : Int{
        return when (idEleccion) {
            "001" -> 1
            "002" -> 2
            "003" -> 3
            "004" -> 4
            else -> 0
        }
    }

    fun EnviarVoto(idUsuario: Int, idCandidato: Int, idEleccion: Int) {

        val url = "https://sigevaback-0rj7.onrender.com/api/votoXCandidato/crear/"

        val datosPost = JSONObject().apply {
            put("idaprendiz", idUsuario)
            put("idcandidatos", idCandidato)
            put("ideleccion", idEleccion)
            put("contador", 1)

        }

        val client = Volley.newRequestQueue(this)
        Log.d("API Voto", "Datos enviados: $datosPost")

        val request = object : JsonObjectRequest(Method.POST, url, datosPost,
            { response ->
                try {
                    val gson = Gson()
                    val data = gson.fromJson(response.toString(), VotoAPI::class.java)

                    val i = Intent(this, VotoExitosoActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    if (data.message.contains("exitosamente", ignoreCase = true)) {
                        // Caso de éxito → ir a pantalla de confirmación
                        startActivity(i)
                    } else {
                        // Caso de error → mostrar modal con mensaje del servidor
                        mostrarModal("Error al votar", data.message)
                    }
                } catch (e: Exception) {
                    mostrarModal("Error parsing", e.message ?: "Error desconocido")
                }
            },{ error ->
                val statusCode = error.networkResponse?.statusCode
                val responseBody = error.networkResponse?.data?.let { String(it, Charsets.UTF_8) }

                Log.e("API Voto", "Status: $statusCode, Body: $responseBody", error)

                try {
                    val gson = Gson()
                    val data = gson.fromJson(responseBody, VotoAPI::class.java)
                    mostrarModal("Error Petición", data.message)
                } catch (e: Exception) {
                    mostrarModal("Error Petición", responseBody ?: "Error desconocido")
                }
            }

        ) {

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        client.add(request)
    }

    fun mostrarModal(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Reintenter") { dialog, _ ->
                EnviarVoto(LoginActivity.aprendiz.id,intent.getIntExtra("idCandidato", 0), formatearVotoStringToInt(idEleccionExtra)) // Todo: Cambir el default value
                dialog.dismiss() }
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .show()
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