package com.example.proyecto_crud.dataclass
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Cliente(
        var nombre_cliente: String?="",
        var matricula_cliente: String?="",
        var numero_telefono_cliente: Int?=0,
        var marca_coche_cliente: String?="",
        var modelo_coche_cliente: String?="",
        var color_coche_cliente: String?="",
        var problema_cliente: String?="",
        var id_taller: String?="",
        var id_cliente: String?="",
        var url_foto_cliente: String?="",
        //mutable list de hash map
        var listamecanicos: MutableList<String>?=null,
//        var listamecanicos: HashMap<String,String>?= null,

    ):Parcelable

