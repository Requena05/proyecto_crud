package com.example.proyecto_crud

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.google.android.material.textfield.TextInputEditText

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AgregarCliente : AppCompatActivity() {
    private var selectedColor: Int = Color.WHITE // Color por defecto
    private lateinit var tallerseleccionado: Spinner
    private lateinit var nombre_cliente: TextInputEditText
    private lateinit var matricula_cliente: TextInputEditText
    private lateinit var telefono_cliente: TextInputEditText
    private lateinit var modelo_coche: TextInputEditText
    private lateinit var marca_coche : TextInputEditText
    private lateinit var problema_coche: TextInputEditText
    private lateinit var colorseleccionado: ImageView
    private lateinit var Botoncolores:AppCompatButton
    private lateinit var antiguedad:String
    private lateinit var boton_crear: AppCompatButton
    //Firebase
    private lateinit var database: DatabaseReference
    //private lateinit var storage: StorageReference
    private var url_matricula: Uri? = null

    //AppWriteStorage
    private lateinit var id_projecto: String
    private lateinit var id_bucket: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_cliente)
        tallerseleccionado=findViewById(R.id.tallerseleccionado)

        val db_ref=FirebaseDatabase.getInstance().reference
        nombre_cliente= findViewById(R.id.nombrecliente)
        matricula_cliente=findViewById(R.id.matriculacliente)
        telefono_cliente=findViewById(R.id.telefonocliente)
        modelo_coche=findViewById(R.id.modelocochecliente)
        marca_coche=findViewById(R.id.marcacochecliente)
        problema_coche=findViewById(R.id.problemacochecliente)
        antiguedad = Util.obtenerFechaActual()
        boton_crear=findViewById(R.id.agregarcliente)
        var activity = this
        //firebase
        database = FirebaseDatabase.getInstance().reference

        //AppWriteStorage
        id_projecto="6759d7920012485d1e95"
        id_bucket="6759d837002a69ef194d"

        var lista_nombretaller: MutableList<String>
        lista_nombretaller=Util.obtenernombreTaller(db_ref,this)

        ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_nombretaller).also{
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tallerseleccionado.adapter=it
        }



        val client = Client()
            .setEndpoint("https://cloud.appwrite.io/v1")    // Your API Endpoint
            .setProject(id_projecto)
//        val listartaller = Utile.obtenerListaTaller(database, this)
        val storage = Storage(client)

        var lista_cliente = Util.obtenerListaCliente(database,this)
        boton_crear.setOnClickListener {
            antiguedad = Util.obtenerFechaActual()
            if ((nombre_cliente.text.toString().isEmpty() || matricula_cliente.text.toString().isEmpty()
                        || telefono_cliente.text.toString().isEmpty()) || modelo_coche.text.toString().isEmpty() || marca_coche.text.toString().isEmpty() || problema_coche.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Rellene todos los campos ",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (Util.existeCliente(
                    lista_cliente,
                    matricula_cliente.text.toString())
            ) {
                Toast.makeText(this, "Cliente con ese coche afectado ya existe", Toast.LENGTH_SHORT)
                    .show()
            }else {
                val identificador_taller = database.child("Motor").child("Talleres").child("nombre").push().key

                GlobalScope.launch(Dispatchers.IO) {
                    val inputStream = contentResolver.openInputStream(url_matricula!!)
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

                    val cliente = Cliente(
                        nombre_cliente.text.toString(),
                        matricula_cliente.text.toString(),
                        telefono_cliente.text.toString().toInt(),
                        marca_coche.text.toString(),
                        modelo_coche.text.toString(),
                        colorseleccionado,
                        problema_coche.text.toString(),
                        identificador_taller
                    )

                    Util.escribirCliente(database, identificador_taller, cliente)
                    Util.tostadaCorrutina(
                        activity, applicationContext,
                        "Cliente creado con exito"
                    )
                }
                finish()
            }
        }

        Botoncolores=findViewById(R.id.openColorPickerButton)
        colorseleccionado=findViewById(R.id.colorseleccionado)
        Botoncolores.setOnClickListener {
            showColorPickerDialog()
        }
    }


    private fun showColorPickerDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.color_picker_dialog, null)
        builder.setView(dialogView)

        val colorGrid: GridLayout = dialogView.findViewById(R.id.colorGrid)

        // Paleta de colores
        val colors = intArrayOf(
            //Añade colores que no esten en esta lista
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA,
            Color.CYAN, Color.GRAY, Color.LTGRAY, Color.DKGRAY, Color.BLACK,
            Color.WHITE,

        )

        // Añadir botones de color al GridLayout
        for (color in colors) {
            val colorButton = Button(this)
            colorButton.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = resources.getDimensionPixelSize(R.dimen.color_button_size)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            colorButton.background = ColorDrawable(color)
            colorButton.setOnClickListener {
                selectedColor = color
                Toast.makeText(this, "Color seleccionado", Toast.LENGTH_SHORT).show()
                colorseleccionado.setColorFilter(selectedColor)
            }
            colorGrid.addView(colorButton)

        }

        val dialog = builder.create()
        dialog.show()

    }




}