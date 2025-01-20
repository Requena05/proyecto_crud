package com.example.proyecto_crud
import android.widget.ImageView
import java.io.Serializable
import java.util.Date

data class Cliente(
        var nombre_cliente: String?="",
        var matricula_cliente: String?="",
        var numero_telefono_cliente: Int?=0,
        var marca_coche_cliente: String?="",
        var modelo_coche_cliente: String?="",
        var color_coche_cliente: ImageView?,
        var problema_cliente: String?="",
        var id_taller: String?="",
    ):Serializable

