package com.victor.sigeva


// TODO: Cambiar los modelso de acuerdo a lo que me devuelva la API

data class Votacion (
    var id : Int? = null,
    var regional : String,
    var centro : String,
    var jornada : String
)

data class Usuario (
    var id : Int? = null,

)

data class Candidato(
    var id: Int? = null,
    var nombre: String,
    var numeroTarjeton: String,
    var programa: String,
    var descripcoin: String
)

