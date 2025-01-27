package com.example.proyecto_crud.partechat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_crud.adaptadores.AdaptadorSeleccionTaller

import com.example.proyecto_crud.databinding.ActivityMensajeBinding
import com.example.proyecto_crud.dataclass.Taller
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MensajeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMensajeBinding
    private lateinit var Logosseleccion: AdaptadorSeleccionTaller
    private lateinit var db_ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityMensajeBinding.inflate(layoutInflater)
        db_ref = FirebaseDatabase.getInstance().getReference()
        setContentView(binding.main)
        var talleres: MutableList<Taller> = mutableListOf()

        //hacemos la nconsul.ta de todos los talleres a firebase y la añdimos al array

        db_ref.child("Motor").child("Talleres").get().addOnSuccessListener {
            if (it.exists()) {
                for (tallerSnapshot in it.children) {
                    val taller = tallerSnapshot.getValue(Taller::class.java)
                    if (taller != null) {
                        talleres.add(taller)
                    }else{
                        Toast.makeText(this,"No hay talleres disponibles",Toast.LENGTH_SHORT)
                    }
                }
            }
//            var imagenestaller: MutableList<String>
//            imagenestaller = mutableListOf()
//
//            talleres.forEach {
//                if(it.id_logo!=null){
//                  imagenestaller.add(it.id_logo!!)
//                }
//            }


            Log.d("Talleres", talleres.size.toString())
            Logosseleccion = AdaptadorSeleccionTaller(talleres, applicationContext)

            binding.elecciontaller.apply {
                //layout horizonbtal
                layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = Logosseleccion

            }
        }
    }
}