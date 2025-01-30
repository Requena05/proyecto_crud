package com.example.proyecto_crud.arreglos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_crud.R
import com.example.proyecto_crud.databinding.ActivityCrearMecanicoBinding
import com.example.proyecto_crud.dataclass.Mecanicos
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearMecanicoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearMecanicoBinding
    private lateinit var nombre: TextInputEditText
    private lateinit var apellido: TextInputEditText
    private lateinit var correo: TextInputEditText
    private lateinit var agregarMecanico: AppCompatButton
    private lateinit var db_ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearMecanicoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nombre = binding.nombremecanico
        apellido = binding.apellidomecanico
        correo = binding.correomecanico
        agregarMecanico = binding.agregarcliente
        db_ref=FirebaseDatabase.getInstance().reference
        agregarMecanico.setOnClickListener {
            val nombre_mecanico = nombre.text.toString()
            val apellido_mecanico = apellido.text.toString()
            val correo_mecanico = correo.text.toString()
            if (nombre_mecanico.isEmpty() || apellido_mecanico.isEmpty() || correo_mecanico.isEmpty()) {
                nombre.error = "Rellene este campo"
                apellido.error = "Rellene este campo"
                correo.error = "Rellene este campo"
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()

            }else if(nombre_mecanico.length>20 || nombre_mecanico.length<3){
                nombre.error="Nombre no valido"
                Toast.makeText(this, "Nombre no valido", Toast.LENGTH_SHORT).show()
            }else if(apellido_mecanico.length>20 || apellido_mecanico.length<3){
                apellido.error="Apellido no valido"
                Toast.makeText(this, "Apellido no valido", Toast.LENGTH_SHORT).show()

                }else if(!correo_mecanico.contains("@") && !correo_mecanico.contains(".")) {
                correo.error = "Correo incorrecto"
                Toast.makeText(this, "Correo incorrecto", Toast.LENGTH_SHORT).show()
            } else {
                val id_mecanico = db_ref.child("Motor").child("Mecanico").push().key
                val mecanico = Mecanicos(nombre_mecanico, apellido_mecanico, correo_mecanico, id_mecanico,mutableListOf(""))
                db_ref.child("Motor").child("Mecanico").child(id_mecanico!!).setValue(mecanico)
                Toast.makeText(this, "Mecanico creado con exito", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

    }
}