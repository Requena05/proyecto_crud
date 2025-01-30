package com.example.proyecto_crud.adaptadores

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_crud.cliente.AgregarCliente
import com.example.proyecto_crud.taller.EditarTallerActivity2
import com.example.proyecto_crud.cliente.ListarClientesActivity
import com.example.proyecto_crud.R
import com.example.proyecto_crud.dataclass.Taller
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.partechat.ChatActivity
import com.google.firebase.database.FirebaseDatabase
import io.appwrite.Client
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdaptadorTaller(private val lista_taller:MutableList<Taller>,last_pos:Int):RecyclerView.Adapter<AdaptadorTaller.TallerViewHolder>(){
    private lateinit var contexto:Context
    private var lista_filtrada=lista_taller
    private var last_pos=last_pos

    inner class TallerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val miniatura: ImageView = itemView.findViewById(R.id.item_miniatura)
        val nombre: TextView = itemView.findViewById(R.id.item_nombre)
        val ciudad: TextView = itemView.findViewById(R.id.item_ciudad)
        val fundacion: TextView = itemView.findViewById(R.id.item_fundacion)
        val editar: ImageView = itemView.findViewById(R.id.item_editar)
        val borrar: ImageView = itemView.findViewById(R.id.item_borrar)
        val rating: RatingBar = itemView.findViewById(R.id.estrellas)
        val agregar: ImageView = itemView.findViewById(R.id.item_add)
        val lista: ImageView = itemView.findViewById(R.id.lista)
        val boton_foro: ImageView = itemView.findViewById(R.id.item_foro)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val vista_item=LayoutInflater.from(parent.context).inflate(R.layout.cartastaller,parent,false)
        contexto=parent.context
        return TallerViewHolder(vista_item)
    }
    override fun getItemCount()=lista_filtrada.size

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val id_projecto="6759d7920012485d1e95"
        val id_bucket="6759d837002a69ef194d"
        val taller_actual=lista_filtrada[position]
        holder.nombre.text=taller_actual.nombre
        holder.ciudad.text=taller_actual.ciudad
        holder.fundacion.text=taller_actual.fundacion.toString()
        holder.rating.rating=taller_actual.rating!!

        holder.boton_foro.setOnClickListener{
            val intent = Intent(contexto, ChatActivity::class.java)
            intent.putExtra("nombre_emisor",taller_actual.nombre)
            intent.putExtra("id_emisor_taller",taller_actual.id)
            intent.putExtra("ciudad_emisor",taller_actual.ciudad)
            intent.putExtra("fundacion_emisor",taller_actual.fundacion)
            intent.putExtra("url_logo_emisor",taller_actual.url_logo)
          contexto.startActivity(intent)
        }



        Log.d("Nombre",taller_actual.nombre.toString())

        val URL:String?=when(taller_actual.url_logo){
            ""->null
            else->taller_actual.url_logo
        }
        Log.d("Url",URL.toString())
        Glide.with(contexto)
            .load(URL)
            .apply(Util.opcionesGlide(contexto))
            .transition(Util.transicion)
            .into(holder.miniatura)

        holder.editar.setOnClickListener{
            val intent = Intent(contexto, EditarTallerActivity2::class.java)
            intent.putExtra("Taller",taller_actual)
            contexto.startActivity(intent)
        }
        holder.borrar.setOnClickListener{
            val db_ref=FirebaseDatabase.getInstance().reference
            db_ref.child("Motor").child("Cliente").get().addOnCompleteListener {
                it.result.children.forEach {
                    if (it.child("id_taller").value.toString()==taller_actual.id){
                        Log.d("id_cliente",it.key.toString())
                        val id_cliente=it.key.toString()
                        db_ref.child("Motor").child("Cliente").child(id_cliente).removeValue()
                    }

                }

            }
            //cuando se borra un taller se borra tambien sus datos en firebase
            val client= Client()
                .setEndpoint("https://cloud.appwrite.io/v1")
                .setProject(id_projecto)

            val storage=Storage(client)
            GlobalScope.launch(Dispatchers.IO){
                storage.deleteFile(
                    bucketId = id_bucket,
                    fileId = taller_actual.id_logo!!
                )
                //Los clientes asociados al taller se borran tambien



            }
            lista_filtrada.removeAt(position)
            db_ref.child("Motor").child("Talleres").child(taller_actual.id!!).removeValue()
            Toast.makeText(contexto,"Taller borrado",Toast.LENGTH_SHORT).show()
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,lista_filtrada.size)
        }

        holder.agregar.setOnClickListener {
            val taller = lista_filtrada[position]
            val intent=Intent(contexto, AgregarCliente::class.java)
            intent.putExtra("Taller",taller.id)
            contexto.startActivity(intent)

        }
        holder.lista.setOnClickListener {
            val taller = lista_filtrada[position]
            val intent = Intent(contexto, ListarClientesActivity::class.java)
            intent.putExtra("Taller", taller.id)
            contexto.startActivity(intent)

        }

    }




}