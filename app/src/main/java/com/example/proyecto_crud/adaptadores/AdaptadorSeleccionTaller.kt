package com.example.proyecto_crud.adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_crud.R
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.dataclass.Taller
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database

class AdaptadorSeleccionTaller(
    private val lista_elecciontaller: MutableList<Taller>,
    private val applicationContext: Context,

) : RecyclerView.Adapter<AdaptadorSeleccionTaller.SeleccionTallerViewHolder>() {

        inner class SeleccionTallerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val imagen_taller: ImageView = itemView.findViewById(R.id.foto_taller)
        }
    private lateinit var db_ref: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeleccionTallerViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.item_seleccion_taller,parent,false)
        return SeleccionTallerViewHolder(vista_item)
    }

    override fun getItemCount(): Int {
      return lista_elecciontaller.size
    }

    override fun onBindViewHolder(holder: SeleccionTallerViewHolder, position: Int) {
         db_ref = FirebaseDatabase.getInstance().reference
         lista_elecciontaller[position].id_logo

        var logo = lista_elecciontaller[position].url_logo
        Glide.with(applicationContext)
            .load(logo)
            .apply(Util.opcionesGlide(applicationContext))
            .transition(Util.transicion)
            .into(holder.imagen_taller)

        Log.d("Logo",logo.toString())


    }

}