package com.example.proyecto_crud.arreglos

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_crud.R
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Mecanicos
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ElegirMecanicoActivity : AppCompatActivity() {
    private lateinit var listaMecanicos: MutableList<String>
    private lateinit var elegido:CheckBox
    private lateinit var db: DatabaseReference
    private lateinit var asociar: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elegir_mecanico)
        db = FirebaseDatabase.getInstance().reference
        listaMecanicos = mutableListOf()
        var id_cliente=intent.getStringExtra("id_cliente")
        db.child("Motor").child("Mecanico").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (mecanicoSnapshot in dataSnapshot.children) {
                    val mecanico = mecanicoSnapshot.getValue(Mecanicos::class.java)
                    Log.d("Mecanico",mecanico.toString())
                    if (mecanico != null ) {
                        if(mecanico.listaclientes!=null){
                            if(mecanico.listaclientes!!.contains(id_cliente)){
                                continue
                            }
                        }else{
                            mecanico.listaclientes= mutableListOf()
                        }
                        listaMecanicos.add(mecanico.id_mecanico!!)
                    }
                }
            }
            Log.d("Mecanicos",listaMecanicos.toString())
            //Ahora creamos en e linear layout con id listaMecanicos los checkbox de los mecanicos para añadirlos al cliente
            for (mecanico in listaMecanicos) {
                elegido = CheckBox(this)
                db.child("Motor").child("Mecanico").child(mecanico).get().addOnSuccessListener { dataSnapshot ->
                    val nombre = dataSnapshot.getValue(Mecanicos::class.java)

                    elegido.text = "${nombre?.nombre_mecanico}"
                    var lista_asociados=mutableListOf<String>()
                    elegido.setOnClickListener {
                        if (elegido.isChecked) {
                            //los mecanicos que esten seleccionados se añaden a la lista_asociados
                            lista_asociados.add(mecanico)
                        }else{
                            lista_asociados.remove(mecanico)
                        }

                    }

                    asociar=findViewById(R.id.asociar)
                    asociar.setOnClickListener {
                        db.child("Motor").child("Cliente").child(id_cliente!!).get().addOnSuccessListener { dataSnapshot ->
                            val cliente_id = dataSnapshot.getValue(Cliente::class.java)?.id_cliente
                            var lista_clientes=mutableListOf<String>()
                            lista_clientes.add(cliente_id!!)


                            db.child("Motor").child("Cliente").child(id_cliente!!)
                                .child("listamecanicos").setValue(lista_asociados)
                            db.child("Motor").child("Mecanico").child(mecanico)
                                .child("listaclientes").setValue(lista_clientes)
                        }
                    }
                    //añadimos la lista_asociados a la lista de clientes del cliente
                }
                findViewById<LinearLayout>(R.id.listaMecanicos).addView(elegido)

            }
        }


    }
}