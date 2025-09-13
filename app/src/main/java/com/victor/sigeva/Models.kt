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
    var votaciones : List<Votacion>
)

data class CandidatoAPI(
    var message: String,
    var Candidatos : List<Candidato>
)



data class Votacion (
    var id : Int? = null,
    var regional : String,
    var centro : String,
    var jornada : String
)


data class Aprendiz(
    var id : Int,
    var nombre : String,
    var apellidos : String,
    var estado : String,
    var perfil : String,
    var jornada : String
)


@Parcelize
data class Candidato(
    var id: Int? = null,
    var nombre: String,
    var numeroTarjeton: String,
    var programa: String,
    var descripcoin: String
) : Parcelable

