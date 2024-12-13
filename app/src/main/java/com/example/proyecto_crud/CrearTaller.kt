package com.example.proyecto_crud

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrearTaller : AppCompatActivity() {
    private lateinit var boton_crear: AppCompatButton
    private lateinit var boton_volver: AppCompatButton
    private lateinit var nombre: EditText
    private lateinit var ciudad: EditText
    private lateinit var logo: ImageView
    private lateinit var fundacion: EditText
    private lateinit var rating: RatingBar


     //Firebase
    private lateinit var database: DatabaseReference
    //private lateinit var storage: StorageReference
    private var url_logo: Uri? = null

    //AppWriteStorage
    private lateinit var id_projecto: String
    private lateinit var id_bucket: String


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
        logo = findViewById(R.id.logo)
        rating = findViewById(R.id.estrellas)

        var activity = this
        //firebase
        database = FirebaseDatabase.getInstance().reference

        //AppWriteStorage
        id_projecto="6759d7920012485d1e95"
        id_bucket="6759d837002a69ef194d"

        val client = Client()
            .setEndpoint("https://cloud.appwrite.io/v1")    // Your API Endpoint
            .setProject(id_projecto)
//        val listartaller = Utile.obtenerListaTaller(database, this)
        val storage = Storage(client)


        var lista_taller = Util.obtenerListaTaller(database, this)
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
            } else if (Util.existeTaller(
                    lista_taller,
                    nombre.text.toString())
            ) {
                Toast.makeText(this, "Taller ya existe", Toast.LENGTH_SHORT).show()
            }else if (rating.rating <=0) {
                Toast.makeText(this, "Seleccione una calificación", Toast.LENGTH_SHORT).show()
            } else {
                val identificador_taller = database.child("Motor").child("Talleres").push().key

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
                        InputFile.fromFile(tempFile) // tenemos un archivo temporal con la imagen
                    }
                    val identificadorFile = ID.unique()
                    val file = storage.createFile(
                        bucketId = id_bucket,
                        fileId = identificadorFile,
                        file = fileImpostor,

                    )

                    var logo =
                        "https://cloud.appwrite.io/v1/storage/buckets/$id_bucket/files/$identificadorFile/preview?project=$id_projecto"

                    val taller = Taller(
                        identificador_taller,
                        nombre.text.toString(),
                        ciudad.text.toString(),
                        fundacion.text.toString().toInt(),
                        logo,
                        identificadorFile,
                        rating.rating
                    )
                    Util.escribirTaller(database, identificador_taller, taller)
                    Util.tostadaCorrutina(
                        activity, applicationContext,
                        "Logo descargado con éxito"
                    )
                }
                finish()
            }
        }
        boton_volver.setOnClickListener {
            finish()
        }

        logo.setOnClickListener {
            accesoGaleria.launch("image/*")
        }
        }
    private val accesoGaleria = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri: Uri? ->
        if (uri != null) {
            url_logo = uri
            logo.setImageURI(url_logo)
        }
    }

    }


