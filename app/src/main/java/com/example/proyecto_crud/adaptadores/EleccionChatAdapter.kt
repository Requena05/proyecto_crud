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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EleccionChatAdapter (private val lista_eleccionchat: MutableList<Cliente>) : RecyclerView.Adapter<EleccionChatAdapter.EleccionChatViewHolder>() {
    private lateinit var contexto: Context
    private lateinit var db_ref: DatabaseReference
    private var lista_filtrada=lista_eleccionchat

    inner class EleccionChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val boton_eleccionchat: ImageView = itemView.findViewById(R.id.contenido)
        val op_eleccionchat: ImageView = itemView.findViewById(R.id.opciones)
        val imagen_eleccionchat: ImageView = itemView.findViewById(R.id.imagen_emisor)
        val nombre_eleccionchat: TextView = itemView.findViewById(R.id.nombre_emisor)
        val tipo_eleccionchat: TextView = itemView.findViewById(R.id.emisor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EleccionChatViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false)
        contexto=parent.context

        return EleccionChatViewHolder(vista_item)
    }

    override fun getItemCount(): Int {
      return lista_eleccionchat.size
    }

    override fun onBindViewHolder(holder: EleccionChatViewHolder, position: Int) {

        db_ref = FirebaseDatabase.getInstance().reference
        val cliente_actual=lista_filtrada[position]
        holder.nombre_eleccionchat.text=cliente_actual.nombre_cliente
        holder.tipo_eleccionchat.text=cliente_actual.matricula_cliente
        holder.imagen_eleccionchat.setImageResource(R.drawable.usuario)
        holder.boton_eleccionchat.setImageResource(R.drawable.comentario)



        holder.boton_eleccionchat.setOnClickListener {
            val intent = Intent(contexto, ChatActivity::class.java)
            intent.putExtra("nombre_emisor",cliente_actual.nombre_cliente)
            intent.putExtra("id_emisor_cliente",cliente_actual)
            db_ref.child("Motor").child("Talleres").child(cliente_actual.id_taller!!).get().addOnSuccessListener {
                if(it.exists()){
                    val taller=it.getValue(Taller::class.java)
                    intent.putExtra("Taller",taller)
                    //intent.putExtra("id_emisor",cliente_actual.id_cliente)
                    contexto.startActivity(intent)
                }
            }

            }


    }

}