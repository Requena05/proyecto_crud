package com.example.proyecto_crud.adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_crud.R
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Mensaje
import com.example.proyecto_crud.dataclass.Taller
import com.example.proyecto_crud.partechat.ChatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MensajeAdaptador(private val lista_mensajes: MutableList<Mensaje>, last_pos: Int) : RecyclerView.Adapter<MensajeAdaptador.MensajeViewHolder>() {
    private lateinit var contexto: Context
    private var last_pos = last_pos
        private lateinit var db_ref: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val vista_item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mensajetaller, parent, false)
        //Para poder hacer referencia al contexto de la aplicacion
        contexto = parent.context

        return MensajeViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val item_actual = lista_mensajes[position]

        holder.pendientes.visibility = View.INVISIBLE
        if (last_pos<lista_mensajes.size-1&&last_pos!=1&&last_pos!=100000&&position==last_pos){
            holder.pendientes.visibility = View.VISIBLE
        }

        var intent= (contexto as ChatActivity).intent
        intent.getStringExtra("item_emisor_cliente")
        intent.getStringExtra("item_emisor_taller")
        if(item_actual.id_receptor==null){
            item_actual.id_receptor=intent.getStringExtra("id_emisor_taller")
        }
        Log.d("Mensaje",item_actual.id_emisor.toString())
        Log.d("Mensaje2",item_actual.id_receptor.toString())

        if(item_actual.id_emisor==item_actual.id_receptor){
            //ES MIO,ASIGNAR A LA DERECHA Y YO
            holder.otro.text=""
            holder.hora_otro.text=""
            holder.imagen_otro.visibility=View.INVISIBLE
            holder.imagen_yo.visibility=View.VISIBLE
            if(item_actual.id_emisor==intent.getStringExtra("id_emisor_taller")){
                db_ref=FirebaseDatabase.getInstance().reference
                var taller_actual=Taller(
                    intent.getStringExtra("id_emisor_taller"),
                    intent.getStringExtra("nombre_emisor"),
                    intent.getStringExtra("ciudad_emisor"),
                    intent.getIntExtra(("fundacion_emisor"),
                        0),
                    intent.getStringExtra("url_logo_emisor"),
                    "",
                    0f,

                )
                val URL:String?=when(taller_actual.url_logo){
                    ""->null
                    else->taller_actual.url_logo
                }
                Log.d("Url",URL.toString())
                Glide.with(contexto)
                    .load(URL)
                    .apply(Util.opcionesGlide(contexto))
                    .transition(Util.transicion)
                    .into(holder.imagen_yo)
            }else{
                val URL:String?="https://cloud.appwrite.io/v1/storage/buckets/6759d837002a69ef194d/files/679a82e0003d1cb091c7/view?project=6759d7920012485d1e95&mode=admin"
                Glide.with(contexto)
                    .load(URL)
                    .apply(Util.opcionesGlide(contexto))
                    .transition(Util.transicion)
                    .into(holder.imagen_yo)
                }
            holder.hora_yo.text=item_actual.fecha_hora
            holder.yo.text=item_actual.contenido
            holder.nombre_yo.text=item_actual.nombre_emisor
            holder.nombre_otro.text=""

        }else{
            //ES DE OTRO ASIGNAR A LA IZQUIERDA Y NOMBRE
            holder.yo.text=""
            holder.hora_yo.text=""
            holder.imagen_yo.visibility=View.INVISIBLE
            holder.imagen_otro.visibility=View.VISIBLE
            if(item_actual.id_receptor==intent.getStringExtra("id_emisor_taller")){
                db_ref=FirebaseDatabase.getInstance().reference
                var taller_actual=Taller(
                    intent.getStringExtra("id_emisor_taller"),
                    intent.getStringExtra("nombre_emisor"),
                    intent.getStringExtra("ciudad_emisor"),
                    intent.getIntExtra(("fundacion_emisor"),
                        0),
                    intent.getStringExtra("url_logo_emisor"),
                    "",
                    0f,

                    )
                val URL:String?=when(taller_actual.url_logo){
                    ""->null
                    else->taller_actual.url_logo
                }
                Log.d("Url",URL.toString())
                Glide.with(contexto)
                    .load(URL)
                    .apply(Util.opcionesGlide(contexto))
                    .transition(Util.transicion)
                    .into(holder.imagen_yo)
            }else{
                val URL:String?="https://cloud.appwrite.io/v1/storage/buckets/6759d837002a69ef194d/files/679a82e0003d1cb091c7/view?project=6759d7920012485d1e95&mode=admin"
                Glide.with(contexto)
                    .load(URL)
                    .apply(Util.opcionesGlide(contexto))
                    .transition(Util.transicion)
                    .into(holder.imagen_yo)
            }

            holder.hora_otro.text=item_actual.fecha_hora
            holder.otro.text=item_actual.contenido
            holder.nombre_otro.text=item_actual.nombre_emisor
            holder.nombre_yo.text=""
        }




    }

    override fun getItemCount(): Int = lista_mensajes.size


    class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val yo: TextView = itemView.findViewById(R.id.yo)
        val otro: TextView = itemView.findViewById(R.id.otro)
        val imagen_otro: ImageView = itemView.findViewById(R.id.imagen_otro)
        val imagen_yo: ImageView = itemView.findViewById(R.id.imagen_yo)
        val hora_yo:TextView = itemView.findViewById(R.id.hora_yo)
        val hora_otro:TextView = itemView.findViewById(R.id.hora_otro)
        val pendientes:ConstraintLayout = itemView.findViewById(R.id.pendientes_mens)
        val nombre_yo:TextView = itemView.findViewById(R.id.nombre_yo)
        val nombre_otro:TextView = itemView.findViewById(R.id.nombre_otro)
    }
}
