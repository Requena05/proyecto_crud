package com.example.proyecto_crud

import java.io.Serializable

data class Taller(
    var id: String? = "",
    var nombre: String? = "",
    var ciudad: String? = "",
    var fundacion: Int? = 0,
    var url_logo: String? = "",
    var id_logo: String? = "",
    var rating: Double? = 0.0
):Serializable
