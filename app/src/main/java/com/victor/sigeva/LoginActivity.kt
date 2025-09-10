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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.input_email)
        val password = findViewById<EditText>(R.id.input_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        startActivity(Intent(this, MainActivity::class.java))

//        btnLogin.setOnClickListener {
//            val url = "http://tu-servidor.com/api/login"
//
//            val queue = Volley.newRequestQueue(this)
//
//            val request = object : StringRequest(
//                Request.Method.POST, url,
//                { response ->
//                    //  respuesta del backend
//                    Toast.makeText(this, "Respuesta: $response", Toast.LENGTH_LONG).show()
//                },
//                { error ->
//                    // Si ocurre un error
//                    Toast.makeText(this, "Error: Completa los campos para ingresar", Toast.LENGTH_LONG).show()
//                }
//            ) {
//                override fun getParams(): MutableMap<String, String> {
//                    val params = HashMap<String, String>()
//                    params["email"] = email.text.toString()
//                    params["password"] = password.text.toString()
//                    return params
//                }
//            }
//
//            queue.add(request)
//        }




    }
}
