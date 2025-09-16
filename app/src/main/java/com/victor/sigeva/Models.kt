package com.victor.sigeva

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class AprendizAPI(
    var message : String,
    var data : Aprendiz
)

data class VotacionesAPI(
    var message: String,
    var eleccionesActivas : List<Votacion>
)



data class Votacion (
    var ideleccion : Int? = null,
    var titulo : String,
    //var regional : String,
    var centro : String,
    var jornada : String?
)

@Parcelize
data class Aprendiz(
    var id : Int,
    var nombre : String,
    var apellidos : String,
    var estado : String,
    var perfil : String,
    var jornada : String?,
    var CentroFormacion : Int
) : Parcelable


data class CentroFormacion (
    var centroFormacioncol : String
)

@Parcelize
data class Candidato(
    var idcandidatos: Int,
    var nombres: String,
    var numeroTarjeton: String,
    //var propuesa: String,
    var propuesta: String,
    var centroFormacion : String,
    var foto : String

) : Parcelable


data class VotoAPI (
    var message : String
)


data class CandidatoAPIListar(
    val message: String,
    val data: List<CandidatoResponse>
)

data class CandidatoResponse(
    val idcandidatos: Int,
    val ideleccion: Int,
    val idaprendiz: Int,
    val numeroTarjeton: String,
    val propuesta: String,
    val foto: String,
    val createdAt: String,
    val updatedAt: String,
    val nombres: String,
    val aprendiz: AprendizResponse
)

data class AprendizResponse(
    val idaprendiz: Int,
    val idgrupo: Int,
    val idprogramaFormacion: Int,
    val perfilIdperfil: Int,
    val centroFormacionIdcentroFormacion: Int,
    val nombres: String,
    val apellidos: String,
    val celular: String,
    val estado: String,
    val tipoDocumento: String,
    val numeroDocumento: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val centro_formacion: CentroFormacionResponse
)

data class CentroFormacionResponse(
    val idcentroFormacion: Int,
    val idregional: Int,
    val centroFormacioncol: String,
    val idmunicipios: Int,
    val direccion: String,
    val telefono: String,
    val correo: String,
    val subdirector: String,
    val correosubdirector: String,
    val createdAt: String,
    val updatedAt: String
)


data class GenerarOTPAPI(
    var message: String,
    var codigo_error : String
)

