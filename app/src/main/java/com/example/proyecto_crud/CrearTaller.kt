package com.example.proyecto_crud

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrearTaller : AppCompatActivity() {
    lateinit var boton_crear: AppCompatButton
    lateinit var boton_volver: AppCompatButton
    lateinit var nombre: EditText
    lateinit var ciudad: EditText
    private lateinit var logo: ImageView
    lateinit var fundacion: EditText

    private var url_logo: Uri? = null
    private lateinit var database: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_taller)
        boton_crear = findViewById(R.id.boton_crear)
        boton_volver = findViewById(R.id.boton_volver)
        nombre = findViewById(R.id.nombre)
        ciudad = findViewById(R.id.ciudad)
        fundacion = findViewById(R.id.fundacion)
        database = FirebaseDatabase.getInstance().reference
        val listartaller = Utile.obtenerListaTaller(database, this)


        boton_volver.setOnClickListener {
            finish()
        }


        boton_crear.setOnClickListener {

            if (nombre.text.isEmpty() || ciudad.text.isEmpty()
                || fundacion.text.isEmpty() || url_logo == null
            ) {
                Toast.makeText(
                    this,
                    "Rellene todos los campos o selecione una imagen",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (fundacion.text.toString().toInt() > 2023 || fundacion.text.toString()
                    .toInt() < 1500
            ) {
                Toast.makeText(this, "Año de fundación no válido", Toast.LENGTH_SHORT).show()
            }else if (Utile.existetaller(listartaller, nombre.text.toString())) {

                Toast.makeText(this, "Ya existe un taller con ese nombre", Toast.LENGTH_SHORT)}
            else {

                val identificador_taller = database.child("nba").child("Talleres").push().key

                GlobalScope.launch(Dispatchers.IO) {
                    val inputStream = contentResolver.openInputStream(url_logo!!)
                    val identificadorAppWrite = identificador_taller!!.substring(1, 20)
                    val fileImpostor = inputStream.use { input ->
                        val tempFile = kotlin.io.path.createTempFile(identificadorAppWrite).toFile()
                        if (input != null) {
                            tempFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        // tenemos un archivo temporal con la imagen

                    }


                }
                finish()
            }
        }


    }

//    val accesoGaleria = registerForActivityResult(ActivityResultContracts.GetContent())
//    { uri: Uri? ->
//        if (uri != null) {
//            url_logo = uri
//            logo.setImageURI(url_logo)
//        }
//    }
}