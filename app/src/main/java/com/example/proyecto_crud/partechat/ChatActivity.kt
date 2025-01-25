package com.example.proyecto_crud.partechat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_crud.R
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.adaptadores.EleccionChatAdapter
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Taller
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.appwrite.Client
import io.appwrite.services.Storage

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EleccionChatAdapter
    private lateinit var lista:MutableList<*>
    private lateinit var db_ref: DatabaseReference
    private lateinit var logotaller: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lista= mutableListOf<Cliente>()
        setContentView(R.layout.activity_chat)
        recyclerView = findViewById(R.id.rview_mensajes)
        adapter = EleccionChatAdapter(lista)

        recyclerView.adapter = adapter
        val id_projecto="6759d7920012485d1e95"
        val id_bucket="6759d837002a69ef194d"
        val client= Client()
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject(id_projecto)
        val storage= Storage(client)
        val intent=intent
        val id_taller=intent.getStringExtra("Taller")
        db_ref= FirebaseDatabase.getInstance().reference



    }
}