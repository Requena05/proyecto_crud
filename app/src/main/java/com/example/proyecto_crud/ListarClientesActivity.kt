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
        lista= mutableListOf()
        setContentView(R.layout.activity_listar_clientes)
        recycler=findViewById(R.id.lista_clientes)
        db_ref= FirebaseDatabase.getInstance().reference
        db_ref.child("Motor")
            .child("Cliente").addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
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
        Log.d("Lista aaaaa",lista.toString())
        adaptador= AdaptadorCliente(lista)
        recycler.adapter=adaptador
        adaptador.notifyDataSetChanged()
        recycler.setHasFixedSize(true)
        recycler.layoutManager=LinearLayoutManager(applicationContext)
        recycler.adapter?.notifyDataSetChanged()

    }
}