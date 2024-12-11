package com.example.proyecto_crud

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class ListarTalleresActivity2 : AppCompatActivity() {
    private lateinit var volver: Button
    private lateinit var recycler: RecyclerView
    private lateinit var lista:MutableList<Taller>
    private lateinit var db_ref: DatabaseReference
    private lateinit var adaptador: AdaptadorTaller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_talleres2)
    }
}