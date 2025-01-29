package com.example.proyecto_crud.partechat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_crud.OnClickListener
import com.example.proyecto_crud.adaptadores.AdaptadorCliente
import com.example.proyecto_crud.adaptadores.AdaptadorSeleccionTaller
import com.example.proyecto_crud.adaptadores.EleccionChatAdapter
import com.example.proyecto_crud.adaptadores.MensajeAdaptador

import com.example.proyecto_crud.databinding.ActivityMensajeBinding
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Taller
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class MensajeActivity : AppCompatActivity(),OnClickListener {
    private lateinit var binding: ActivityMensajeBinding
    private lateinit var Logosseleccion: AdaptadorSeleccionTaller
    private lateinit var db_ref: DatabaseReference
    private lateinit var recycler: RecyclerView
    private lateinit var recycler2: RecyclerView
    private lateinit var lista: MutableList<Cliente>
    private lateinit var adaptador: EleccionChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lista = mutableListOf()
        binding = ActivityMensajeBinding.inflate(layoutInflater)
        db_ref = FirebaseDatabase.getInstance().getReference()
        setContentView(binding.main)
        var talleres: MutableList<Taller> = mutableListOf()
        recycler2 = binding.rviewMensajes
        recycler = binding.elecciontaller
        //hacemos la nconsul.ta de todos los talleres a firebase y la añdimos al array

        db_ref.child("Motor").child("Talleres").get().addOnSuccessListener {
            if (it.exists()) {
                for (tallerSnapshot in it.children) {
                    val taller = tallerSnapshot.getValue(Taller::class.java)
                    if (taller != null) {
                        talleres.add(taller)
                    } else {
                        Toast.makeText(this, "No hay talleres disponibles", Toast.LENGTH_SHORT)
                    }
                }
            }
            Log.d("Total Talleres", talleres.size.toString())
            Logosseleccion = AdaptadorSeleccionTaller(talleres,applicationContext,this)

            binding.elecciontaller.apply {
                //layout horizonbtal
                layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = Logosseleccion
                recycler2.adapter?.notifyDataSetChanged()
                setHasFixedSize(true)
                //se tiene que poder clickar en las fotos de los talleres y actualizar el otro recycler con los clientes asociados
            }
        }
    }

    override fun onClick(taller: Taller) {
        lista = mutableListOf()
        db_ref = FirebaseDatabase.getInstance().getReference()
        db_ref.child("Motor").child("Cliente").get().addOnSuccessListener {
            if (it.exists()) {
                for (clienteSnapshot in it.children) {
                    val cliente = clienteSnapshot.getValue(Cliente::class.java)
                    Log.d("IdTaller",taller.id.toString())
                    Log.d("IdCliente",cliente?.id_taller.toString())
                    if (cliente?.id_taller != null ) {
                        if (cliente.id_taller == taller.id) {
                            lista.add(cliente)

                        }

                    }
                }
                //si se hace click en el mismo taller no se añade a la lista
                adaptador = EleccionChatAdapter(lista)
                binding.rviewMensajes.apply {
                    layoutManager = LinearLayoutManager(applicationContext)
                    adapter = adaptador
                }
            }
            Log.d("Listatodo", lista.size.toString())

        }

    }
}