package com.victor.sigeva

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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"

class MasInformacionCandidatoFragment : BottomSheetDialogFragment() {
    private var param1: Candidato? = null

    lateinit var nombreCandidatoFragment : TextView
    lateinit var numeroCandidatoFragment : TextView
    lateinit var descripcionCandidatoFragment : TextView
    lateinit var btnCancelarFragment : Button
    lateinit var btnVotarFragment : Button
    lateinit var imagenCandidatoFragment : ImageView


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

        Glide.with(view.context).load(param1?.foto).error(R.drawable.candidato_default).into(imagenCandidatoFragment)


        btnCancelarFragment.setOnClickListener {
            dismiss()
        }

        btnVotarFragment.setOnClickListener {
            generarOtp(LoginActivity.aprendiz.id, SeleccionCandidatosActivity.idEleccionSeleccionCandidato)
        }

    }

    fun generarOtp(idAprendiz: Int, idEleccion: Int) {
        val url = "https://sigevaback-real.onrender.com/api/validaciones/generarOtp/"

        val client = Volley.newRequestQueue(requireContext())

        val parametros = JSONObject().apply {
            put("aprendiz_idaprendiz", idAprendiz)
            put("elecciones_ideleccion", idEleccion)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, parametros,
            { response ->
                try {
                    val mensaje = response.getString("message")

                    // ✅ Caso éxito (con data)
                    if (response.has("data")) {
                        val data = response.getJSONObject("data")
                        val codigoOtp = data.getString("codigo_otp_temporal")

                        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()

                        // Intent hacia ValidacionOTPActivity
                        val intent = Intent(requireContext(), ConfirmarVotoActivity::class.java).apply {
                            putExtra("codigoOtp", codigoOtp)
                            putExtra("idCandidato", param1?.idcandidatos)
                            putExtra("email", data.getString("email_enviado_a"))
                        }
                        startActivity(intent)

                    } else {
                        // ❌ Caso error lógico
                        val codigoError = response.optString("codigo_error", "SIN_CODIGO")
                        val detalles = response.optJSONObject("detalles")

                        var infoExtra = ""
                        if (detalles != null) {
                            val fechaInicio = detalles.optString("fecha_inicio")
                            val fechaActual = detalles.optString("fecha_actual")
                            infoExtra = "\nInicio: $fechaInicio\nHoy: $fechaActual"
                        }

                        Toast.makeText(requireContext(), "Error: $mensaje\nCódigo: $codigoError$infoExtra", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error al procesar respuesta: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error en la petición: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        client.add(request)
    }


    companion object {

        var idEleccionMasInformacion : String? = null

        @JvmStatic
        fun newInstance(candidato: Candidato) =
            MasInformacionCandidatoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, candidato)
                }
            }
    }
}