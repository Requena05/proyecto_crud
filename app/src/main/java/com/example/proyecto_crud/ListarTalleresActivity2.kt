package com.example.proyecto_crud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListarTalleresActivity2<MyApplication> : AppCompatActivity() {
    private lateinit var volver: Button
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Taller>
    private lateinit var db_ref: DatabaseReference
    private lateinit var busqueda: TextInputEditText
    private lateinit var adaptador: AdaptadorTaller
    private lateinit var filtrar: Spinner
    private lateinit var boton_buscar: ImageView
    private var lista_filtradaa= mutableListOf<Taller>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_talleres2)
        volver=findViewById(R.id.volver_inicio)
        recycler=findViewById(R.id.lista_talleres)
        filtrar=findViewById(R.id.filtrar)
        boton_buscar=findViewById(R.id.boton_buscar)
        busqueda=findViewById(R.id.busquedapornombre)



        //Rellenar el spinner con dato
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
                boton_buscar.setOnClickListener {
                    Log.d("boton_buscar","click")
                    val filtro=filtrar.selectedItem.toString()
                    when(filtro){
                        "Filtrar por rating"->{
                            lista.sortByDescending { it.rating }
                            recycler.adapter?.notifyDataSetChanged()
                        }
                        "Filtrar en orden descendente"->{
                            lista.sortByDescending { it.fundacion }
                            recycler.adapter?.notifyDataSetChanged()
                        }
                        "Filtrar en orden ascendente"->{
                            lista.sortBy { it.fundacion }
                            recycler.adapter?.notifyDataSetChanged()
                        }
                    }
                }

                recycler.adapter?.notifyDataSetChanged()
                //con buscar se filtra por nombre en tiempo real y se actualiza la lista
                busqueda.doOnTextChanged { text, start, count, after ->
                    //filtramos la lista por nombre letra por letra
                    lista_filtradaa=lista.filter { it.nombre!!.contains(text.toString(),true) } as MutableList<Taller>
                    recycler.adapter=AdaptadorTaller(lista_filtradaa)
                    recycler.adapter?.notifyDataSetChanged()
                    if(text.toString().isEmpty()){
                        recycler.adapter=AdaptadorTaller(lista)
                        recycler.adapter?.notifyDataSetChanged()
                    }

                }


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