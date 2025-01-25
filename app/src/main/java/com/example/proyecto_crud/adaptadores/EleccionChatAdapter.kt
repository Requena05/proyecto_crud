package com.example.proyecto_crud.adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_crud.R
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Taller
import com.example.proyecto_crud.partechat.ChatActivity
import com.google.firebase.database.FirebaseDatabase

class EleccionChatAdapter (private val lista_eleccionchat: MutableList<*>) : RecyclerView.Adapter<EleccionChatAdapter.EleccionChatViewHolder>() {
    private lateinit var contexto: Context
    private lateinit var db_ref: FirebaseDatabase
    private var lista_filtrada=lista_eleccionchat

    inner class EleccionChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val boton_eleccionchat: ImageView = itemView.findViewById(R.id.contenido)
        val op_eleccionchat: ImageView = itemView.findViewById(R.id.opciones)
        val imagen_eleccionchat: ImageView = itemView.findViewById(R.id.imagen_emisor)
        val nombre_eleccionchat: TextView = itemView.findViewById(R.id.nombre_emisor)
        val tipo_eleccionchat: TextView = itemView.findViewById(R.id.tipo_emisor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EleccionChatViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.cartascliente,parent,false)
        contexto=parent.context

        return EleccionChatViewHolder(vista_item)
    }

    override fun getItemCount(): Int {
      return lista_eleccionchat.size
    }

    override fun onBindViewHolder(holder: EleccionChatViewHolder, position: Int) {

        db_ref = FirebaseDatabase.getInstance()
        val cliente_actual=lista_filtrada[position]
        val taller_actual=lista_filtrada[position]

        if (cliente_actual is Cliente) {
            holder.boton_eleccionchat.setImageResource(R.drawable.comentario)
            holder.op_eleccionchat.setImageResource(R.drawable.trespunto)
            holder.imagen_eleccionchat.setImageResource(R.drawable.usuario)
            holder.nombre_eleccionchat.text=cliente_actual.nombre_cliente
            holder.tipo_eleccionchat.text=cliente_actual.matricula_cliente
            holder.boton_eleccionchat.setOnClickListener {
                val intent = Intent(contexto, ChatActivity::class.java)
                contexto.startActivity(intent)
            }
        }else if (taller_actual is Taller){
            holder.boton_eleccionchat.setImageResource(R.drawable.comentario)
            holder.op_eleccionchat.setImageResource(R.drawable.trespunto)
            holder.imagen_eleccionchat.setImageResource(R.drawable.logoflashfix)
            holder.nombre_eleccionchat.text=taller_actual.nombre
            holder.tipo_eleccionchat.text=taller_actual.ciudad
        }


    }

}