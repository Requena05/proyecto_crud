package com.example.proyecto_crud.partechat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_crud.R
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.adaptadores.EleccionChatAdapter
import com.example.proyecto_crud.adaptadores.MensajeAdaptador
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Mensaje
import com.example.proyecto_crud.dataclass.Taller
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.appwrite.Client
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.CountDownLatch

class ChatActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var lista: MutableList<Mensaje>
    private lateinit var db_ref: DatabaseReference
    private lateinit var mensaje_enviado: EditText
    private lateinit var boton_enviar: Button
    private lateinit var taller_actual: Taller
    private var last_pos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        taller_actual = intent.getParcelableExtra<Taller>("Taller")!!
        last_pos = intent.getIntExtra("LAST_POS", 100000)
        Log.d("LASTTT_POS_LLEGAMOS", last_pos.toString())
        db_ref = FirebaseDatabase.getInstance().getReference()
        lista = mutableListOf()
        mensaje_enviado = findViewById(R.id.texto_mensaje)
        boton_enviar = findViewById(R.id.boton_enviar)

        boton_enviar.setOnClickListener {
            last_pos = 1
            val mensaje = mensaje_enviado.text.toString().trim()

            if (mensaje.trim() != "") {
                val hoy: Calendar = Calendar.getInstance()
                val formateador: SimpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                val fecha_hora = formateador.format(hoy.getTime());

                val id_mensaje = db_ref.child("chat").child("mensajes").push().key!!
                val nuevo_mensaje = Mensaje(
                    id_mensaje,
                    taller_actual.id,
                    "",
                    "",
                    mensaje,
                    fecha_hora,
                )
                db_ref.child("chat").child("mensajes").child(id_mensaje).setValue(nuevo_mensaje)
                mensaje_enviado.setText("")
            } else {
                Toast.makeText(applicationContext, "Escribe algo", Toast.LENGTH_SHORT).show()
            }

        }
        db_ref.child("chat").child("mensajes").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                GlobalScope.launch(Dispatchers.IO) {
                    val pojo_mensaje = snapshot.getValue(Mensaje::class.java)
                    pojo_mensaje!!.id_receptor = taller_actual.id
                    if(pojo_mensaje.id_receptor==pojo_mensaje.id_emisor){
                        pojo_mensaje.imagen_emisor=taller_actual.url_logo
                    }else{

                        var semaforo = CountDownLatch(1)


                        db_ref.child("Motor").child("Talleres").child(pojo_mensaje.id_emisor!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val taller = snapshot.getValue(Taller::class.java)
                                    pojo_mensaje.imagen_emisor = taller!!.url_logo
                                    semaforo.countDown()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    println(error.message)
                                }
                            })
                        semaforo.await()
                    }

                    runOnUiThread {
                        lista.add(pojo_mensaje)
                        lista.sortBy { it.fecha_hora }
                        recycler.adapter!!.notifyDataSetChanged()
                        if (last_pos<lista.size&&last_pos!=1&&last_pos!=100000){
                            recycler.scrollToPosition((last_pos))
                        }else{
                            recycler.scrollToPosition((lista.size-1))
                        }


                    }
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        recycler = findViewById(R.id.rview_mensajes)
        recycler.adapter = MensajeAdaptador(lista, last_pos)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.setHasFixedSize(true)



    }
    override fun onBackPressed() {
        finish()
        val actividad = Intent(applicationContext,MensajeActivity::class.java)
        last_pos = lista.size
        actividad.putExtra("LAST_POS", last_pos)
        Log.d("LASTTT_POS_ATRAS",last_pos.toString())
        startActivity (actividad)
    }

}