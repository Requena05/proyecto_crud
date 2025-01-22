package com.example.proyecto_crud

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import io.appwrite.Client
import io.appwrite.services.Storage

class AdaptadorCliente(private val lista_cliente:MutableList<Cliente>): RecyclerView.Adapter<AdaptadorCliente.ClienteViewHolder>() {
    private lateinit var contexto: Context
    private var lista_filtrada=lista_cliente

    private lateinit var db_ref: FirebaseDatabase

    inner class ClienteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.item_nombre)
        val matricula: TextView = itemView.findViewById(R.id.item_matricula)
        val telefono: TextView = itemView.findViewById(R.id.item_telefono)
        val marca: TextView = itemView.findViewById(R.id.item_marcacoche)
        val modelo: TextView = itemView.findViewById(R.id.item_modelo)
        val color: ImageView = itemView.findViewById(R.id.item_colorcoche)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.cartascliente,parent,false)
        contexto=parent.context

        return ClienteViewHolder(vista_item)
    }

    override fun getItemCount(): Int {
     return lista_filtrada.size
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        db_ref = FirebaseDatabase.getInstance()
                    val cliente_actual=lista_filtrada[position]
                    holder.nombre.text=cliente_actual!!.nombre_cliente
                    holder.matricula.text=cliente_actual.matricula_cliente
                    holder.telefono.text=cliente_actual.numero_telefono_cliente.toString()
                    holder.marca.text=cliente_actual.marca_coche_cliente
                    holder.modelo.text=cliente_actual.modelo_coche_cliente
                    holder.color.setColorFilter(cliente_actual.color_coche_cliente!!.toInt())

    }


}