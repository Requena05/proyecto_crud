package com.example.proyecto_crud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListarTalleresActivity2 : AppCompatActivity() {
    private lateinit var volver: Button
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Taller>
    private lateinit var db_ref: DatabaseReference
    private lateinit var adaptador: AdaptadorTaller
    private lateinit var filtrar: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_talleres2)
        volver=findViewById(R.id.volver_inicio)
        recycler=findViewById(R.id.lista_talleres)
        filtrar=findViewById(R.id.filtrar)
        //Rellenar el spinner con datos
         val lista_filtros=resources.getStringArray(R.array.filtros)
        filtrar.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,lista_filtros)
        db_ref=FirebaseDatabase.getInstance().reference
        lista= mutableListOf()
        db_ref.child("Motor")
            .child("Talleres").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                snapshot.children.forEach{hijo:DataSnapshot?->
                    val pojo_taller=hijo?.getValue(Taller::class.java)
                    lista.add(pojo_taller!!)
                }
                filtrar.setOnClickListener {
                    when(filtrar.selectedItemPosition){
                        0->{
                            lista.sortBy { it.rating }
                        }
                        1->{
                            lista.sortBy { it.fundacion }
                            lista.reverse()
                        }
                        2->{
                            lista.sortBy { it.fundacion }
                        }
                        3->{
                            lista.sortBy { it.fundacion }
                        }
                    }

                }
                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    adaptador= AdaptadorTaller(lista)
    recycler.adapter=adaptador
    recycler.setHasFixedSize(true)
    recycler.layoutManager=LinearLayoutManager(applicationContext)
    volver.setOnClickListener{
        finish()
    }
    }

}