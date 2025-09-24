package com.victor.sigeva

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"

class MasInformacionCandidatoFragment : BottomSheetDialogFragment() {
    private var param1: Candidato? = null

    private lateinit var nombreCandidatoFragment: TextView
    private lateinit var numeroCandidatoFragment: TextView
    private lateinit var descripcionCandidatoFragment: TextView
    private lateinit var btnCancelarFragment: Button
    private lateinit var btnVotarFragment: Button
    private lateinit var imagenCandidatoFragment: ImageView

    private val requestQueue by lazy {
        context?.let { Volley.newRequestQueue(it) }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1, Candidato::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mas_informacion_candidato, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nombreCandidatoFragment = view.findViewById(R.id.nombreCandidatosFragment)
        numeroCandidatoFragment = view.findViewById(R.id.numeroCandidatosFragment)
        descripcionCandidatoFragment = view.findViewById(R.id.descripcionCandidatoFragment)
        btnVotarFragment = view.findViewById(R.id.btnVotarFragment)
        btnCancelarFragment = view.findViewById(R.id.btnCancelarFragment)
        imagenCandidatoFragment = view.findViewById(R.id.imagenCandidadoFragment)

        nombreCandidatoFragment.text = param1?.nombres ?: "Nombre no encontrado"
        numeroCandidatoFragment.text = "00${param1?.numeroTarjeton}" ?: "000"
        descripcionCandidatoFragment.text = param1?.propuesta ?: "Descripcion no encontrada"

        Glide.with(view.context)
            .load(param1?.foto)
            .error(R.drawable.candidato_default)
            .into(imagenCandidatoFragment)

        btnCancelarFragment.setOnClickListener {
            dismiss()
        }

        btnVotarFragment.setOnClickListener {
            generarOtp(
                LoginActivity.aprendiz.id,
                SeleccionCandidatosActivity.idEleccionSeleccionCandidato
            )
        }
    }

    private fun generarOtp(idAprendiz: Int, idEleccion: Int) {
        val url = "https://sigevaback-real.onrender.com/api/validaciones/generarOtp/"

        val parametros = JSONObject().apply {
            put("aprendiz_idaprendiz", idAprendiz)
            put("elecciones_ideleccion", idEleccion)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, parametros,
            { response ->
                if (!isAdded) return@JsonObjectRequest // 🔒 si el fragmento ya no está, salir

                try {
                    val mensaje = response.getString("message")

                    if (response.has("data")) {
                        val data = response.getJSONObject("data")
                        val codigoOtp = data.getString("codigo_otp_temporal")

                        context?.let {
                            Toast.makeText(it, mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(it, ConfirmarVotoActivity::class.java).apply {
                                putExtra("codigoOtp", codigoOtp)
                                putExtra("idCandidato", param1?.idcandidatos)
                                putExtra("email", data.getString("email_enviado_a"))
                            }
                            startActivity(intent)
                        }

                    } else {
                        val codigoError = response.optString("codigo_error", "SIN_CODIGO")
                        val detalles = response.optJSONObject("detalles")

                        var infoExtra = ""
                        if (detalles != null) {
                            val fechaInicio = detalles.optString("fecha_inicio")
                            val fechaActual = detalles.optString("fecha_actual")
                            infoExtra = "\nInicio: $fechaInicio\nHoy: $fechaActual"
                        }

                        context?.let {
                            Toast.makeText(
                                it,
                                "Error: $mensaje\nCódigo: $codigoError$infoExtra",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    context?.let {
                        Toast.makeText(
                            it,
                            "Error al procesar respuesta: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            { error ->
                if (!isAdded) return@JsonObjectRequest


                context?.let {
                    mostrarModal(it)
                    Toast.makeText(it, "Error en la petición: ${error.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        )

        requestQueue?.add(request)
    }

    companion object {
        var idEleccionMasInformacion: String? = null

        @JvmStatic
        fun newInstance(candidato: Candidato) =
            MasInformacionCandidatoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, candidato)
                }
            }
    }
}

fun mostrarModal(context : Context) {
    var dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_peticion_otp, null)

    var botonAceparModal = dialogView.findViewById<Button>(R.id.btnModalAceptarOTPError)
    var dialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .create()
    botonAceparModal.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()


}

