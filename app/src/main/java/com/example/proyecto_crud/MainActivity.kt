package com.example.proyecto_crud

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.proyecto_crud.arreglos.ArreglosActivity2
import com.example.proyecto_crud.arreglos.CrearMecanicoActivity
import com.example.proyecto_crud.taller.ListarTalleresActivity2
import com.example.proyecto_crud.partechat.MensajeActivity
import com.example.proyecto_crud.taller.CrearTaller

class MainActivity : AppCompatActivity() {
    lateinit var crearTaller: AppCompatButton
    lateinit var listarTalleres: AppCompatButton
    lateinit var chat: AppCompatButton
    private lateinit var arreglo: AppCompatButton
    private lateinit var crearMecanico: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        crearTaller = findViewById(R.id.crearTaller)
        listarTalleres = findViewById(R.id.listarTalleres)
        arreglo = findViewById(R.id.Arreglos)
        crearMecanico = findViewById(R.id.crearmecanico)
        chat = findViewById(R.id.chat)
        crearTaller.setOnClickListener {
            val intent = Intent(this, CrearTaller::class.java)
            startActivity(intent)

        }

        listarTalleres.setOnClickListener {
        val intent=Intent(this, ListarTalleresActivity2::class.java)
            startActivity(intent)
        }
        chat.setOnClickListener {
                val intent=Intent(this, MensajeActivity::class.java)
            startActivity(intent)
        }
        arreglo.setOnClickListener {
            val intent=Intent(this, ArreglosActivity2::class.java)
            startActivity(intent)
        }
        crearMecanico.setOnClickListener {
            val intent=Intent(this, CrearMecanicoActivity::class.java)
            startActivity(intent)
        }



    }
}