package com.victor.sigeva

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.collection.floatIntMapOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val ARG_PARAM1 = "param1"

class MasInformacionCandidatoFragment : BottomSheetDialogFragment() {
    private var param1: Candidato? = null

    lateinit var nombreCandidatoFragment : TextView
    lateinit var programaCandidatoFragment : TextView
    lateinit var numeroCandidatoFragment : TextView
    lateinit var descripcionCandidatoFragment : TextView
    lateinit var btnCancelarFragment : Button
    lateinit var btnVotarFragment : Button


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



        //nombreCandidatoFragment.text = param1?.nombres ?: "Nombre no encontrado"
        //programaCandidatoFragment.text = param1?.programa ?: "Programa no encontrado"
        numeroCandidatoFragment.text = param1?.numeroTarjeton ?: "000"
        descripcionCandidatoFragment.text = param1?.propuesta ?: "Descripcion no encontrada"


        btnCancelarFragment.setOnClickListener {
            dismiss()
        }

        btnVotarFragment.setOnClickListener {
            var i = Intent(view.context, ConfirmarVotoActivity::class.java)
            i.putExtra("idCandidato", param1?.idcandidatos)
            i.putExtra("idEleccion", numeroCandidatoFragment.text.toString())
            startActivity(i)
        }



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