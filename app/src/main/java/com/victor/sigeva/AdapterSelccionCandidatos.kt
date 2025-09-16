package com.victor.sigeva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterSelccionCandidatos(var listaCandidatoResponses : List<Candidato>, var onClickCandidato: (Candidato) -> Unit) : RecyclerView.Adapter<AdapterSelccionCandidatos.ViewHolderCandidatos>() {
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
        var item = listaCandidatoResponses[position]

        holder.nombreCandidato.text = item.nombres
        //holder.programaCandidato.text = LoginActivity.aprendiz.
        holder.descripcionCandidato.text = item.propuesta
        holder.numeroTarjeton.text = "00${item.numeroTarjeton}"
        Glide.with(holder.imagenCandidato.context).load(item.foto).error(R.drawable.candidato_default).into(holder.imagenCandidato)
        holder.btnVotar.setOnClickListener {
            onClickCandidato(item)
        }
    }

    override fun getItemCount(): Int {
        return listaCandidatoResponses.size
    }

    class  ViewHolderCandidatos(view : View) : RecyclerView.ViewHolder(view){
        var nombreCandidato = view.findViewById<TextView>(R.id.nombreItemCandidatos)

        var descripcionCandidato = view.findViewById<TextView>(R.id.descripcionItemCandidato)
        var numeroTarjeton = view.findViewById<TextView>(R.id.numeroItemCandidatos)
        var btnVotar = view.findViewById<Button>(R.id.btnVotarCandidatoItemCandidatos)
        var imagenCandidato = view.findViewById<ImageView>(R.id.imageItemCandidatos)
    }
    fun ActulizarAdapter(newList : List<Candidato>) {
        listaCandidatoResponses = newList
        notifyDataSetChanged()
    }

}