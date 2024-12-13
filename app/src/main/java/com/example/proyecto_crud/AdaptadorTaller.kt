package com.example.proyecto_crud

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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import io.appwrite.Client
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class AdaptadorTaller(private val lista_taller:MutableList<Taller>):RecyclerView.Adapter<AdaptadorTaller.TallerViewHolder>(){
    private lateinit var contexto:Context
    private var lista_filtrada=lista_taller

    inner class TallerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val miniatura: ImageView = itemView.findViewById(R.id.item_miniatura)
        val nombre: TextView = itemView.findViewById(R.id.item_nombre)
        val ciudad: TextView = itemView.findViewById(R.id.item_ciudad)
        val fundacion: TextView = itemView.findViewById(R.id.item_fundacion)
        val editar: ImageView = itemView.findViewById(R.id.item_editar)
        val borrar: ImageView = itemView.findViewById(R.id.item_borrar)
        val rating: RatingBar = itemView.findViewById(R.id.estrellas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val vista_item=LayoutInflater.from(parent.context).inflate(R.layout.cartastaller,parent,false)
        contexto=parent.context
        return TallerViewHolder(vista_item)
    }

    override fun getItemCount()=lista_filtrada.size

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val taller_actual=lista_filtrada[position]
        holder.nombre.text=taller_actual.nombre
        holder.ciudad.text=taller_actual.ciudad
        holder.fundacion.text=taller_actual.fundacion.toString()
        holder.rating.rating=taller_actual.rating!!

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
            val intent = Intent(contexto,EditarTallerActivity2::class.java)
            intent.putExtra("Taller",taller_actual)
            contexto.startActivity(intent)
        }
        holder.borrar.setOnClickListener{
            val db_ref=FirebaseDatabase.getInstance().reference
            val id_projecto="6759d7920012485d1e95"
            val id_bucket="6759d837002a69ef194d"

            val client= Client()
                .setEndpoint("https://cloud.appwrite.io/v1")
                .setProject(id_projecto)

            val storage=Storage(client)
            GlobalScope.launch(Dispatchers.IO){
                storage.deleteFile(
                    bucketId = id_bucket,
                    fileId = taller_actual.id_logo!!
                )
            }
            lista_filtrada.removeAt(position)
            db_ref.child("Motor").child("Talleres").child(taller_actual.id_logo!!).removeValue()
            Toast.makeText(contexto,"Taller borrado",Toast.LENGTH_SHORT).show()
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,lista_filtrada.size)
        }
    }
}