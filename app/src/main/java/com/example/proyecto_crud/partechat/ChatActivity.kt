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
    private var last_pos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        var taller_actual = intent.getParcelableExtra<Taller>("id_emisor_taller")
        var usuario_actual = intent.getParcelableExtra<Cliente>("id_emisor_cliente")
        val intent = intent

        Log.d("id_emisor", taller_actual.toString())
        Log.d("id_emisor2", usuario_actual.toString())
        //Log.d("id_emisor3", item_actual.toString())

        last_pos = intent.getIntExtra("LAST_POS", 100000)
        Log.d("LASTTT_POS_LLEGAMOS", last_pos.toString())
        db_ref = FirebaseDatabase.getInstance().getReference()
        lista = mutableListOf()
        mensaje_enviado = findViewById(R.id.texto_mensaje)
        boton_enviar = findViewById(R.id.boton_enviar)

        boton_enviar.setOnClickListener {
            last_pos = 1
            val mensaje = mensaje_enviado.text.toString().trim()
        if(usuario_actual==null){
            if (mensaje.trim() != "") {
                val hoy: Calendar = Calendar.getInstance()
                val formateador: SimpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                val fecha_hora = formateador.format(hoy.getTime());
                val nombre_emisor = intent.getStringExtra("nombre_emisor")
                val id_mensaje = db_ref.child("Motor").child("Mensajes").push().key
                val nuevo_mensaje = Mensaje(
                    id_mensaje,
                    taller_actual?.id,
                    "",
                    "",
                    mensaje,
                    fecha_hora,
                    nombre_emisor,
                )
                db_ref.child("Motor").child("mensajes").child(id_mensaje!!).setValue(nuevo_mensaje)
                mensaje_enviado.setText("")
            } else {
                Toast.makeText(applicationContext, "Escribe algo", Toast.LENGTH_SHORT).show()
            }
        }else if(taller_actual==null){
            if (mensaje.trim() != "") {
                val hoy: Calendar = Calendar.getInstance()
                val formateador: SimpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                val fecha_hora = formateador.format(hoy.getTime());
                val nombre_emisor = intent.getStringExtra("nombre_emisor")
                val id_mensaje = db_ref.child("Motor").child("Mensajes").push().key
                val nuevo_mensaje = Mensaje(
                    id_mensaje,
                    usuario_actual?.id_cliente,
                    "",
                    "",
                    mensaje,
                    fecha_hora,
                    nombre_emisor,
                )
                db_ref.child("Motor").child("mensajes").child(id_mensaje!!).setValue(nuevo_mensaje)
                mensaje_enviado.setText("")
            } else {
                Toast.makeText(applicationContext, "Escribe algo", Toast.LENGTH_SHORT).show()
            }
        }


        }
        db_ref.child("Motor").child("mensajes").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                GlobalScope.launch(Dispatchers.IO) {
                    val pojo_mensaje = snapshot.getValue(Mensaje::class.java)
                    pojo_mensaje!!.id_receptor = usuario_actual?.id_cliente
                    if(pojo_mensaje.id_receptor==pojo_mensaje.id_emisor){
                        pojo_mensaje.imagen_emisor=usuario_actual?.url_foto_cliente
                    }else{

                        var semaforo = CountDownLatch(1)


                        db_ref.child("Motor").child("Cliente").child(pojo_mensaje.id_emisor!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val cliente = snapshot.getValue(Cliente::class.java)
                                    pojo_mensaje.imagen_emisor = cliente?.url_foto_cliente
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
    override fun onResume() {
        super.onResume()
        recycler.adapter!!.notifyDataSetChanged()
    }


}