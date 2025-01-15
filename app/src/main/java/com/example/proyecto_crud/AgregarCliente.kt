package com.example.proyecto_crud

import android.graphics.Color
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AgregarCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_cliente)
        val colorPalette = findViewById<RadioGroup>(R.id.colorPalette)

        colorPalette.setOnCheckedChangeListener { group, checkednombre ->
            val selectedColor = when (checkednombre) {
                R.id.colorRed -> Color.RED
                R.id.colorGreen -> Color.GREEN
                R.id.colorBlue -> Color.BLUE
                R.id.colorYellow -> Color.YELLOW
                R.id.colorPurple -> Color.parseColor("#800080") // Puedes usar hexadecimal
                else -> Color.BLACK // Color por defecto
            }

            // Aquí puedes usar el color seleccionado
            Toast.makeText(this, "Color seleccionado", Toast.LENGTH_SHORT).show()
        }
    }
}