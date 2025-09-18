package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import org.json.JSONObject

class ConfirmarVotoActivity : AppCompatActivity() {

    lateinit var et1: EditText
    lateinit var et2: EditText
    lateinit var et3: EditText
    lateinit var et4: EditText
    lateinit var et5: EditText
    lateinit var et6: EditText

    lateinit var btnConfirmarVoto: MaterialButton
    lateinit var idEleccionExtra: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_voto)

        btnConfirmarVoto = findViewById(R.id.btnConfirmarVoto)

        val idEleccionInt = SeleccionCandidatosActivity.idEleccionSeleccionCandidato

        if (idEleccionInt != 0) {
            idEleccionExtra = String.format("%03d", idEleccionInt)
        } else {
            Toast.makeText(
                this,
                "Error: No se pudo obtener información de la elección",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        et1 = findViewById(R.id.et1)
        et2 = findViewById(R.id.et2)
        et3 = findViewById(R.id.et3)
        et4 = findViewById(R.id.et4)
        et5 = findViewById(R.id.et5)
        et6 = findViewById(R.id.et6)

        btnConfirmarVoto.setOnClickListener {
            val codigoOtp = et1.text.toString() + et2.text.toString() +
                    et3.text.toString() + et4.text.toString() +
                    et5.text.toString() + et6.text.toString()

            if (codigoOtp.length == 6) {
                validarOtp(codigoOtp)
            } else {
                Toast.makeText(
                    this,
                    "Debes ingresar los 6 dígitos del código",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setupOtpInputs()
    }

    fun formatearVotoStringToInt(idEleccion: String): Int {
        return when (idEleccion) {
            "001" -> 1
            "002" -> 2
            "003" -> 3
            "004" -> 4
            "005" -> 5
            "006" -> 6
            "007" -> 7
            "008" -> 8
            "009" -> 9
            "010" -> 10
            "011" -> 11
            "012" -> 12
            else -> 0
        }
    }

    fun validarOtp(codigoOtp: String) {
        val url = "https://sigevaback-real.onrender.com/api/validaciones/validarOtp"

        val datosPost = JSONObject().apply {
            put("codigo_otp", codigoOtp)
        }

        val client = Volley.newRequestQueue(this)

        val request = object : JsonObjectRequest(
            Method.POST, url, datosPost,
            { response ->
                    val success = response.optBoolean("success", false)
                    val message = response.optString("message", "Sin mensaje")

                    if (message.contains("validado", ignoreCase = true)) {
                        EnviarVoto(
                            LoginActivity.aprendiz.id,
                            intent.getIntExtra("idCandidato", 0),
                            formatearVotoStringToInt(idEleccionExtra)
                        )
                    } else {
                        ControllerModales.MostrarModalGeneral(this,
                            "Código Incorrecto",
                            "El código que ingresaste no es válido o ha expirado.",
                            R.drawable.codigo_incorrecto) {
                        }
                    }


            }, { error ->
                val responseBody = error.networkResponse?.data?.let { String(it, Charsets.UTF_8) }
                Log.e("API OTP", "Error: $responseBody", error)
                ControllerModales.MostrarModalGeneral(this,
                    "Código Incorrecto",
                    "El código que ingresaste no es válido o ha expirado.",
                    R.drawable.codigo_incorrecto) {
                }
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        client.add(request)
    }

    fun EnviarVoto(idUsuario: Int, idCandidato: Int, idEleccion: Int) {
        val url = "https://sigevaback-real.onrender.com/api/votoXCandidato/crear/"

        val datosPost = JSONObject().apply {
            put("idaprendiz", idUsuario)
            put("idcandidatos", idCandidato)
            put("ideleccion", idEleccion)
            put("contador", 1)
        }

        val client = Volley.newRequestQueue(this)
        Log.d("API Voto", "Datos enviados: $datosPost")

        val request = object : JsonObjectRequest(
            Method.POST, url, datosPost,
            { response ->
                val i = Intent(this, VotoExitosoActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                    val gson = Gson()
                    val data = gson.fromJson(response.toString(), VotoAPI::class.java)

                    if (data.message.contains("exitosamente", ignoreCase = true)) {
                        startActivity(i)
                    } else if (data.message.contains("ya realizaste", ignoreCase = true)) {

                        ControllerModales.MostrarModalGeneral(this,
                            "Voto Duplicado",
                            "Nuestro sistema detecta que ya emitiste tu voto en esta elección. No es posible registrar un nuevo voto.",
                            R.drawable.voto_duplicado) {
                            val intent = Intent(this, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        ControllerModales.MostrarModalGeneral(this,
                            "Error en el Servidor",
                            "Ocurrió un problema al procesar tu solicitud. Puede deberse a una falla temporal en el servidor.",
                            R.drawable.error_servidor) {
                            val codigoOtp = et1.text.toString() + et2.text.toString() +
                                    et3.text.toString() + et4.text.toString() +
                                    et5.text.toString() + et6.text.toString()
                            if (codigoOtp.length == 6) validarOtp(codigoOtp)
                        }
                    }


            }, { error ->
                val statusCode = error.networkResponse?.statusCode
                val responseBody = error.networkResponse?.data?.let { String(it, Charsets.UTF_8) }

                Log.e("API Voto", "Status: $statusCode, Body: $responseBody", error)

                try {
                    val gson = Gson()
                    val data = gson.fromJson(responseBody, VotoAPI::class.java)
                    ControllerModales.MostrarModalGeneral(this,
                        "Error en el Servidor",
                        "Ocurrió un problema al procesar tu solicitud. Puede deberse a una falla temporal en el servidor.",
                        R.drawable.error_servidor) {
                        val codigoOtp = et1.text.toString() + et2.text.toString() +
                                et3.text.toString() + et4.text.toString() +
                                et5.text.toString() + et6.text.toString()
                        if (codigoOtp.length == 6) validarOtp(codigoOtp)
                    }
                } catch (e: Exception) {
                    ControllerModales.MostrarModalGeneral(this,
                        "Error en la Peticion",
                        "No se pudo completar la solicitud (GET/POST). Puede deberse a un problema de conexión o a un error en el servidor.",
                        R.drawable.error_peticion) {
                        val codigoOtp = et1.text.toString() + et2.text.toString() +
                                et3.text.toString() + et4.text.toString() +
                                et5.text.toString() + et6.text.toString()
                        if (codigoOtp.length == 6) validarOtp(codigoOtp)
                    }
                }
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        client.add(request)
    }
//
//    fun mostrarModal(titulo: String, mensaje: String) {
//        runOnUiThread {
//            val builder = AlertDialog.Builder(this)
//                .setTitle(titulo)
//                .setMessage(mensaje)
//                .setCancelable(false)
//
//            if (mensaje.contains("ya realizaste", ignoreCase = true)) {
//                builder.setPositiveButton("Aceptar") { dialog, _ ->
//                    val intent = Intent(this, MainActivity::class.java).apply {
//                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    }
//                    startActivity(intent)
//                    dialog.dismiss()
//                    finish()
//                }
//            } else {
//                builder.setPositiveButton("Reintentar") { dialog, _ ->
//                    val codigoOtp = et1.text.toString() + et2.text.toString() +
//                            et3.text.toString() + et4.text.toString() +
//                            et5.text.toString() + et6.text.toString()
//                    if (codigoOtp.length == 6) validarOtp(codigoOtp)
//                    dialog.dismiss()
//                }
//                builder.setNegativeButton("Cancelar") { dialog, _ ->
//                    dialog.dismiss()
//                }
//            }
//
//            builder.show()
//        }
//    }

    private fun setupOtpInputs() {
        val editTexts = listOf(et1, et2, et3, et4, et5, et6)

        for (i in editTexts.indices) {
            val editText = editTexts[i]
            editText.filters = arrayOf(InputFilter.LengthFilter(1))

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty()) {
                        val upperChar = s.toString().uppercase()
                        if (s.toString() != upperChar) {
                            editText.setText(upperChar)
                            editText.setSelection(1)
                        }
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    } else {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }
}
