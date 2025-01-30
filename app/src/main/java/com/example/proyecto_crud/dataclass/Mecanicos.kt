package com.example.proyecto_crud.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mecanicos(
    var nombre_mecanico:String?=null,
    var apellidos_mecanico:String?=null,
    var correo_mecanico:String?=null,
    var id_mecanico:String?=null,
    var listaclientes:MutableList<String>?=null,
):Parcelable