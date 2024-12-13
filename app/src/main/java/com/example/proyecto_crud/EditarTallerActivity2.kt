package com.example.proyecto_crud

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditarTallerActivity2 : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var nombre: EditText
    private lateinit var ciudad: EditText
    private lateinit var fundacion: EditText
    private lateinit var boton_modificar: Button
    private lateinit var boton_volver: Button
    private lateinit var rating: RatingBar

    private lateinit var database: DatabaseReference
    //private lateinit var storage: StorageReference
    private var url_logo: Uri? = null
    private lateinit var taller: Taller
    private lateinit var lista_taller: MutableList<Taller>

    //AppWriteStorage
    private lateinit var id_projecto: String
    private lateinit var id_bucket: String
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_club2)

        logo = findViewById(R.id.escudo)
        nombre = findViewById(R.id.nombre)
        ciudad = findViewById(R.id.ciudad)
        fundacion = findViewById(R.id.fundacion)
        boton_modificar = findViewById(R.id.boton_modificar)
        boton_volver = findViewById(R.id.boton_volver)
        rating = findViewById(R.id.estrellas)
        database = FirebaseDatabase.getInstance().reference
        //storage = FirebaseStorage.getInstance().reference

        taller = intent.getSerializableExtra("Taller") as Taller

        nombre.setText(taller.nombre)
        ciudad.setText(taller.ciudad)
        rating.rating=taller.rating!!
        fundacion.setText(taller.fundacion.toString())
        id_projecto="6759d7920012485d1e95"
        id_bucket="6759d837002a69ef194d"
        val client=Client()
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject(id_projecto)
        //val storage=FirebaseStorage.getInstance()
        //val storage=Firebase.storage.reference
        val storage=Storage(client)
        var activity=this
        Glide.with(applicationContext)
            .load(taller.url_logo)
            .apply(Util.opcionesGlide(applicationContext))
            .transition(Util.transicion)
            .into(logo)

        logo.setOnClickListener{
            accesoGaleria.launch("image/*")
        }
        lista_taller=Util.obtenerListaTaller(database,this)
        boton_modificar.setOnClickListener {
            if (nombre.text.isEmpty() || ciudad.text.isEmpty()
                || fundacion.text.isEmpty() || taller.rating==null
            ) {
                Toast.makeText(
                    this,
                    "Rellene todos los campos o selecione una imagen",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (fundacion.text.toString().toInt() > 2023 || fundacion.text.toString()
                    .toInt() < 1500
            ) {
                Toast.makeText(this,"Año de fundacion no válido",Toast.LENGTH_SHORT).show()
            }else if(Util.existeTaller(
                lista_taller,
                nombre.text.toString()
            ) && nombre.text.toString().lowercase() != taller.nombre!!.lowercase()){
                Toast.makeText(this, "Taller ya existe", Toast.LENGTH_SHORT).show()

            } else{
                val identificador_taller=taller.id

                GlobalScope.launch(Dispatchers.IO){
                    if(url_logo!=null){
                        Log.d("URL",url_logo.toString())
                        val identificadorFile= ID.unique()
                        Log.d("IDDDDD",identificadorFile)
                        Log.d("IDANTIGUA",taller.id_logo!!)
                        storage.deleteFile(
                            bucketId = id_bucket,
                            fileId = taller.id_logo!!
                        )
                        val inputStream=contentResolver.openInputStream(url_logo!!)
                        val fileImpostor=inputStream.use { input->
                            val tempfile=kotlin.io.path.createTempFile(identificadorFile).toFile()
                            if(input!=null){
                                tempfile.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                        }
                            InputFile.fromFile(tempfile)
                        }
                        val file=storage.createFile(
                            bucketId = id_bucket,
                            fileId = identificadorFile,
                            file = fileImpostor,
                        )
                        taller.id_logo=identificadorFile
                        var logo=
                            "https://cloud.appwrite.io/v1/storage/buckets/$id_bucket/files/$identificadorFile/preview?project=$id_projecto"
                        taller.url_logo=logo
                    }else if(nombre.text.toString()!=taller.nombre || ciudad.text.toString()!= taller.ciudad ||
                        fundacion.text.toString().toInt()!=taller.fundacion!! || rating.rating!=taller.rating!!){
                        val Taller=Taller(
                            identificador_taller,
                            nombre.text.toString(),
                            ciudad.text.toString(),
                            fundacion.text.toString().toInt(),
                            taller.url_logo,
                            taller.id_logo,
                            rating.rating
                        )
                        Util.escribirTaller(database,identificador_taller!!,Taller)
                        Util.tostadaCorrutina(activity,applicationContext,
                            "Taller actualizado con éxito")
                    }
                }
                finish()
            }
        }
        boton_volver.setOnClickListener {
            finish()
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