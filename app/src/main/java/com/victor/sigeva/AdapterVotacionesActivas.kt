package com.victor.sigeva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterVotacionesActivas(var listaVotaciones : List<Votacion>, var onClickButotn: (Votacion) -> Unit) : RecyclerView.Adapter<AdapterVotacionesActivas.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterVotacionesActivas.ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_votaciones_activas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AdapterVotacionesActivas.ViewHolder,
        position: Int
    ) {
        var item = listaVotaciones[position]
        holder.regional.text = item.regional
        holder.jornada.text = item.jornada
        holder.centro.text = item.centro
        holder.boton.setOnClickListener {
            onClickButotn(item)
        }




    }

    override fun getItemCount(): Int {
        return listaVotaciones.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var regional = view.findViewById<TextView>(R.id.titleItemVotation)
        var centro = view.findViewById<TextView>(R.id.centroItemVotation)
        var jornada = view.findViewById<TextView>(R.id.jornadaItemVotation)
        var boton = view.findViewById<Button>(R.id.btnParctiparVotaciones)

    }

}