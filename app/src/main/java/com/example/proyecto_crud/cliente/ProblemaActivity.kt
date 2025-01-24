package com.example.proyecto_crud.cliente

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.proyecto_crud.R
import com.example.proyecto_crud.dataclass.Cliente
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProblemaActivity : AppCompatActivity() {
    private lateinit var editar_problema: AppCompatButton
    private lateinit var eliminar_problema: AppCompatButton
    private lateinit var contenedor_problema: TextView
    private lateinit var Cabecera: TextView
    private lateinit var cliente:MutableList<Cliente>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_problema)
        editar_problema = findViewById(R.id.editar_problema)
        eliminar_problema = findViewById(R.id.eliminar_problema)
        contenedor_problema = findViewById(R.id.contenedor_problema)
        Cabecera = findViewById(R.id.Cabecera)
        cliente= mutableListOf()

        //recoger el problema del cliente de la base de datos
        val matricula=intent.getStringExtra("Cliente").toString()
        Log.d("matricula",matricula)
        val db_ref=FirebaseDatabase.getInstance().reference
       db_ref.child("Motor").child("Cliente").get().addOnSuccessListener {
           for (snapshot in it.children){
               if (snapshot.child("matricula_cliente").value.toString()==matricula){
                   val problema=snapshot.child("problema_cliente").value.toString()
                   contenedor_problema.text=problema

               }

           }
       }


        //recojer los datos del cliente de la base de datos con el id del cliente
        eliminar_problema.setOnClickListener {


            db_ref.child("Motor").child("Cliente").get().addOnCompleteListener {
                for (snapshot in it.result.children){
                    if (snapshot.child("matricula_cliente").value.toString()==matricula){
                        val id_cliente=snapshot.key.toString()
                        db_ref.child("Motor").child("Cliente").child(id_cliente).removeValue()
                        break
                    }
                }
            }


            val probar = db_ref.child("Motor").child("Cliente").child("matricula_cliente").get().addOnSuccessListener {
                Log.d("problema",it.toString())
            }
                Toast.makeText(this,"Problema eliminado",Toast.LENGTH_SHORT).show()
                Log.d("problema",probar.result.toString())


            finish()
        }
        editar_problema.setOnClickListener {
           finish()


        }


    }
}