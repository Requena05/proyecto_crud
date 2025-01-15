package com.example.proyecto_crud
import java.io.Serializable
import java.util.Date

data class Cliente(
        var nombre_cliente: String?="",
        var matricula_cliente: String?="",
        var numero_telefono_cliente: String?="",
        var marca_coche_cliente: String?="",
        var modelo_coche_cliente: String?="",
        var color_coche_cliente: String?="",
        var problema_cliente: String?="",
        var fecha_llegada: Date?=null,
        var id_taller: String?="",
    ):Serializable

