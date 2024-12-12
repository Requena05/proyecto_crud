package com.example.proyecto_crud

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var crearTaller: AppCompatButton
    lateinit var listarTalleres: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        crearTaller = findViewById(R.id.crearTaller)
        listarTalleres = findViewById(R.id.listarTalleres)

        crearTaller.setOnClickListener {
            val intent = Intent(this, CrearTaller::class.java)
            startActivity(intent)

        }

        listarTalleres.setOnClickListener {
        val intent=Intent(this, ListarTalleresActivity2::class.java)
            startActivity(intent)
        }
    }
}