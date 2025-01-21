package com.example.proyecto_crud

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Taller(
    var id: String? = "",
    var nombre: String? = "",
    var ciudad: String? = "",
    var fundacion: Int? = 0,
    var url_logo: String? = "",
    var id_logo: String? = "",
    var rating: Float? = 0.0f,
    var fecha_creado:String?="",
    var matriculas_clientes:MutableList<String>?= mutableListOf()

):Parcelable
