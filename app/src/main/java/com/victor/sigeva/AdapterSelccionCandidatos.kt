package com.victor.sigeva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterSelccionCandidatos(var listaCandidatos : List<Candidato>, var onClickCandidato: (Candidato) -> Unit) : RecyclerView.Adapter<AdapterSelccionCandidatos.ViewHolderCandidatos>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSelccionCandidatos.ViewHolderCandidatos {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view_seleccion_candidato, parent, false)
        return ViewHolderCandidatos(view)
    }

    override fun onBindViewHolder(
        holder: AdapterSelccionCandidatos.ViewHolderCandidatos,
        position: Int
    ) {
        var item = listaCandidatos[position]

        holder.nombreCandidato.text = item.nombre
        holder.programaCandidato.text = item.programa
        holder.descripcionCandidato.text = item.descripcoin
        holder.numeroTarjeton.text = item.numeroTarjeton.toString()
        holder.btnVotar.setOnClickListener {
            onClickCandidato(item)
        }
    }

    override fun getItemCount(): Int {
        return listaCandidatos.size
    }

    class  ViewHolderCandidatos(view : View) : RecyclerView.ViewHolder(view){
        var nombreCandidato = view.findViewById<TextView>(R.id.nombreItemCandidatos)
        var programaCandidato = view.findViewById<TextView>(R.id.programaItemCandidato)
        var descripcionCandidato = view.findViewById<TextView>(R.id.descripcionItemCandidato)
        var numeroTarjeton = view.findViewById<TextView>(R.id.numeroItemCandidatos)
        var btnVotar = view.findViewById<Button>(R.id.btnVotarCandidatoItemCandidatos)
    }

}