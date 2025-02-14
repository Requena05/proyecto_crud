package com.example.proyecto_crud.dataclass

import android.os.Parcelable

import kotlinx.parcelize.Parcelize
@Parcelize
data class Mensaje(var id:String?=null,
                   var id_emisor:String?=null,
                   var id_receptor:String?=null,
                   var imagen_emisor:String?=null,
                   var contenido:String?=null,
                   var fecha_hora:String?=null,
                   var nombre_emisor:String?=null,): Parcelable