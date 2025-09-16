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
    lateinit var programaCandidatoFragment : TextView
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
        programaCandidatoFragment = view.findViewById(R.id.programaCandidatoFragment)
        numeroCandidatoFragment = view.findViewById(R.id.numeroCandidatosFragment)
        descripcionCandidatoFragment = view.findViewById(R.id.descripcionCandidatoFragment)
        btnVotarFragment = view.findViewById(R.id.btnVotarFragment)
        btnCancelarFragment = view.findViewById(R.id.btnCancelarFragment)
        imagenCandidatoFragment = view.findViewById(R.id.imagenCandidadoFragment)



        nombreCandidatoFragment.text = param1?.nombres ?: "Nombre no encontrado"
        //programaCandidatoFragment.text = param1?.programa ?: "Programa no encontrado"
        numeroCandidatoFragment.text = "00${param1?.numeroTarjeton}" ?: "000"
        descripcionCandidatoFragment.text = param1?.propuesta ?: "Descripcion no encontrada"

        Glide.with(view.context).load(param1?.foto).error(R.drawable.candidato_default).into(imagenCandidatoFragment)


        btnCancelarFragment.setOnClickListener {
            dismiss()
        }

        btnVotarFragment.setOnClickListener {
            var i = Intent(view.context, ConfirmarVotoActivity::class.java)
            i.putExtra("idCandidato", param1?.idcandidatos)
            i.putExtra("idEleccion", numeroCandidatoFragment.text.toString())

            obtenerCodigoOTPParaAprendiz(LoginActivity.aprendiz.id, SeleccionCandidatosActivity.idEleccionSeleccionCandidato)

            startActivity(i)
        }

    }

    fun obtenerCodigoOTPParaAprendiz(idAprendiz: Int, idEleccion : Int) {
        var url = "https://sigevaback-real.onrender.com/api/validaciones/generarOtp"
        var client = Volley.newRequestQueue(requireContext())
        var parametros = JSONObject().apply {
            put("aprendiz_idaprendiz", idAprendiz)
            put("elecciones_ideleccion", idEleccion)
        }



        var request = JsonObjectRequest(Request.Method.POST, url, parametros,
            { response ->
                var gson = Gson()
                var data = gson.fromJson(response.toString(), GenerarOTPAPI::class.java)



        }, { error ->
            
        })
    }


    companion object {

        @JvmStatic
        fun newInstance(candidato: Candidato) =
            MasInformacionCandidatoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, candidato)
                }
            }
    }
}