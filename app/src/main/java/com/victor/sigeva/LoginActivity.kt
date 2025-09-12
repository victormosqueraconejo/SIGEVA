package com.victor.sigeva

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


// TODO: Para hacer validaciones tener en cuenta
// 1. Si el usuario no existe, mostrar el respectivo mensaje
// 2. Si falla la peticion, mostrar el mensaje para que lo vuelva a intentar




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


    // Me retorna unicamente la inforamcion de usuario

    fun GetInformacionAprendiz(nombreCuenta : String, password : String, ValidarData : (AprendizAPI) -> Unit) {
        var url = ""
        var client = Volley.newRequestQueue(this)

        var parametros = JSONObject()
        parametros.put("email", nombreCuenta)
        parametros.put("password", password)

        var request = JsonObjectRequest(Request.Method.POST, url, parametros, {
                response ->
            val gson = Gson()
            var data = gson.fromJson(response.toString(), AprendizAPI::class.java)
            ValidarData(data)


        }, { error ->
            Toast.makeText(this, "Error: ${error.message.toString()}", Toast.LENGTH_SHORT).show() // Cambiar por un mensaje mejor
        })

        client.add(request)
    }

    fun ValidarRespuesta(data : AprendizAPI) {
        if (data != null) {
            if (data.message == "Autenticado") {
                Toast.makeText(this, "Autenticado con exito.", Toast.LENGTH_SHORT).show()
                aprendiz = data.data
                startActivity(Intent(this, MainActivity::class.java))
            }
            else {
                Toast.makeText(this, "Error al autenticar.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error al hacer la peticion.", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        lateinit var aprendiz : Aprendiz
    }

}

