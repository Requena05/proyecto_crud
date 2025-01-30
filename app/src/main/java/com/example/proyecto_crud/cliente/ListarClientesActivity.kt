package com.example.proyecto_crud.cliente

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_crud.R
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.adaptadores.AdaptadorCliente
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Taller
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.appwrite.Client
import io.appwrite.services.Storage

class ListarClientesActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Cliente>
    private lateinit var db_ref: DatabaseReference
    private lateinit var contexto: Context
    private lateinit var adaptador: AdaptadorCliente
    private lateinit var logotaller: ImageView
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lista= mutableListOf()
        setContentView(R.layout.activity_listar_clientes)
        recycler=findViewById(R.id.lista_clientes)
        logotaller=findViewById(R.id.logo_taller)
        contexto=this
        //con la imagen del taller se cambia el imageview del logo_taller

        val id_projecto="6759d7920012485d1e95"
        val id_bucket="6759d837002a69ef194d"
        val client= Client()
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject(id_projecto)
        val storage= Storage(client)
        val intent=intent
        val id_taller=intent.getStringExtra("Taller")
        db_ref= FirebaseDatabase.getInstance().reference
        db_ref.child("Motor").child("Talleres").child(id_taller!!).addValueEventListener(object : ValueEventListener {
            //obtenemos la imagen del taller con el id del taller que nos pasan por intent y la mostramos en el imageview
            override fun onDataChange(snapshot: DataSnapshot) {
                val pojo_taller=snapshot.getValue(Taller::class.java)
                if(pojo_taller!=null){
                    val URL:String?=when(pojo_taller!!.url_logo){
                        ""->null
                        else->pojo_taller.url_logo
                    }
                    Glide.with(applicationContext)
                        .load(URL)
                        .apply(Util.opcionesGlide(applicationContext))
                        .transition(Util.transicion)
                        .into(logotaller)
                }

            }

            override fun onCancelled(error: DatabaseError) {
               print(error.message)
            }

        })






        db_ref= FirebaseDatabase.getInstance().reference
        db_ref.child("Motor")
            .child("Cliente").addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach { hijo:DataSnapshot?  ->
                        val pojo_cliente = hijo?.getValue(Cliente::class.java)
                        Log.d("Cliente",pojo_cliente.toString())
                        adaptador.notifyDataSetChanged()
                        if (pojo_cliente != null) {
                            if(pojo_cliente.id_taller==intent.getStringExtra("Taller")){
                                lista.add(pojo_cliente)
                            }
                        }

                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        adaptador= AdaptadorCliente(lista)
        recycler.adapter=adaptador
        adaptador.notifyDataSetChanged()
        recycler.setHasFixedSize(true)
        recycler.layoutManager=LinearLayoutManager(applicationContext)
        recycler.adapter?.notifyDataSetChanged()

    }
    override fun onResume() {
        super.onResume()
        recycler.adapter?.notifyDataSetChanged()
        //si existe un cliente con la matricula null en la base de datos se eliminara
        adaptador.notifyDataSetChanged()

    }
}