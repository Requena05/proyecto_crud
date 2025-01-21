package com.example.proyecto_crud

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class ListarClientesActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Cliente>
    private lateinit var db_ref: DatabaseReference
    private lateinit var adaptador: AdaptadorCliente

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_clientes)
        recycler=findViewById(R.id.lista_clientes)
        lista= mutableListOf()
        db_ref= FirebaseDatabase.getInstance().reference
        db_ref.child("Motor")
            .child("Cliente").addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach { hijo: DataSnapshot? ->
                        val pojo_cliente = hijo?.getValue(Cliente::class.java)
                        lista.add(pojo_cliente!!)
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        adaptador= AdaptadorCliente(lista)
        Log.d("AdaptadorCliente",lista.toString())
        recycler.adapter=adaptador
        adaptador.notifyDataSetChanged()
        recycler.setHasFixedSize(true)
        recycler.layoutManager=LinearLayoutManager(applicationContext)
        recycler.adapter?.notifyDataSetChanged()

    }
}