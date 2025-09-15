package com.victor.sigeva

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



// TODO: Cambiar los modelso de acuerdo a lo que me devuelva la API

// Modelos Respuestas API

data class AprendizAPI(
    var message : String,
    var data : Aprendiz
)

data class VotacionesAPI(
    var message: String,
    var eleccionesActivas : List<Votacion>
)

data class CandidatoAPI(
    var message: String,
    var data : List<Candidato>
)



data class Votacion (
    var ideleccion : Int? = null,
    var idCentroFormacion : Int,
    //var regional : String,
    var nombre : String,
    //var jornada : String
)


data class Aprendiz(
    var id : Int,
    var nombres : String,
    var apellidos : String,
    var estado : String,
    var perfil : String,
    var jornada : String
)


@Parcelize
data class Candidato(
    var idcandidatos: Int? = null,
    //var nombres: String,
    var numeroTarjeton: String,
    //var propuesa: String,
    var propuesta: String
) : Parcelable


data class VotoAPI (
    var message : String
)
