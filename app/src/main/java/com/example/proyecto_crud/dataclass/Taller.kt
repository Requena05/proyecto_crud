package com.example.proyecto_crud.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    var id_cliente:String?=""

):Parcelable
