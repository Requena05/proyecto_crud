package com.example.proyecto_crud

import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Taller

interface OnClickListener {
    //Cuando el usuario pulse en la foto del taller saldran en el otro recycler sus clientes asociados
    fun onClick(taller: Taller)



}