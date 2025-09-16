package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.input_email)
        val password = findViewById<EditText>(R.id.input_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            GetInformacionAprendiz(email.text.toString(), password.text.toString()) { data ->
                ValidarRespuesta(data)
            }
        }
    }

    // Me retorna unicamente la informacion de usuario
    fun GetInformacionAprendiz(nombreCuenta : String, password : String, ValidarRespuesta : (AprendizAPI) -> Unit) {
        val url = "https://sigevaback-real.onrender.com/api/aprendiz/login/"
        val client = Volley.newRequestQueue(this)

        val parametros = JSONObject()
        parametros.put("email", nombreCuenta)
        parametros.put("password", password)

        val request = JsonObjectRequest(Request.Method.POST, url, parametros, { response ->
            val gson = Gson()
            val data = gson.fromJson(response.toString(), AprendizAPI::class.java)
            ValidarRespuesta(data)

        }, { error ->
            Log.d("API", error.toString())
            mostrarModal("Error", "No se pudo conectar con el servidor. Inténtalo de nuevo.")
        })

        client.add(request)
    }

    fun ValidarRespuesta(data : AprendizAPI) {
        if (data.message == "Autenticado") {
            Toast.makeText(this, "Autenticado con éxito.", Toast.LENGTH_SHORT).show()

            // CORRECCIÓN: Inicializar aprendiz ANTES de cambiar de actividad
            aprendiz = Aprendiz(data.data.id, data.data.nombre, data.data.apellidos, data.data.estado, data.data.perfil, data.data.jornada, data.data.CentroFormacion)
            Log.d("Aprendiz" , aprendiz.toString())

            // CORRECCIÓN: Mover startActivity DESPUÉS de inicializar aprendiz
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Opcional: para que no se pueda volver a LoginActivity con el botón atrás

        } else {
            mostrarModal("Error de autenticación", "Correo o contraseña incorrectos.")
        }
    }

    // Modal
    fun mostrarModal(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    companion object {
        lateinit var aprendiz : Aprendiz
    }
}