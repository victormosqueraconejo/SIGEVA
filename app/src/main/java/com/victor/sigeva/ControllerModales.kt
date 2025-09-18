package com.victor.sigeva

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

object ControllerModales {


    // Parametros:
    // Contexto : Es Obligatorio
    // imagen : Se le pasa el id del recurso
    // Se usa una lamba para la accion del boton

    fun MostrarModalGeneral(context: Context, title : String, description :String, imagen : Int, onClickGreen : () -> Unit) {
        var view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_general, null)

        var imageGeneral = view.findViewById<ImageView>(R.id.imagenModalGeneral)
        var titleGeneral = view.findViewById<TextView>(R.id.titleModalGeneral)
        var descriptionGeneral = view.findViewById<TextView>(R.id.descriptionModalGeneral)
        var btnRojo = view.findViewById<Button>(R.id.btnModalCancelar)
        var btnVerde = view.findViewById<Button>(R.id.btnModalAceptar)



        var dialog = AlertDialog.Builder(context).setView(view).create()

        imageGeneral.setImageResource(imagen)
        titleGeneral.text = title
        descriptionGeneral.text = description

        btnRojo.setOnClickListener {
            dialog.dismiss()
        }

        btnVerde.setOnClickListener {
            onClickGreen()
            dialog.dismiss()
        }

        dialog.show()
    }

}