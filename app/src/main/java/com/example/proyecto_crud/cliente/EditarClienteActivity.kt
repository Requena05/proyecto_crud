package com.example.proyecto_crud.cliente

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_crud.R
import com.example.proyecto_crud.Util
import com.example.proyecto_crud.dataclass.Cliente
import com.example.proyecto_crud.dataclass.Taller
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.appwrite.Client
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditarClienteActivity : AppCompatActivity() {
    private var selectedColor: Int = Color.WHITE // Color por defecto
    private lateinit var contexto: Context
    private lateinit var nombre_cliente: TextInputEditText
    private lateinit var matricula_cliente: TextInputEditText
    private lateinit var telefono_cliente: TextInputEditText
    private lateinit var modelo_coche: TextInputEditText
    private lateinit var marca_coche : TextInputEditText
    private lateinit var problema_coche: TextInputEditText
    private lateinit var colorseleccionado: ImageView

    private lateinit var Botoncolores: AppCompatButton
    private lateinit var antiguedad:String
    private lateinit var boton_crear: AppCompatButton
    //Firebase
    private lateinit var database: DatabaseReference

    //AppWriteStorage
    private lateinit var id_projecto: String
    private lateinit var id_bucket: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_cliente)
        contexto=this
        val db_ref= FirebaseDatabase.getInstance().reference
        nombre_cliente= findViewById(R.id.nombrecliente)
        matricula_cliente=findViewById(R.id.matriculacliente)
        telefono_cliente=findViewById(R.id.telefonocliente)
        modelo_coche=findViewById(R.id.modelocochecliente)
        marca_coche=findViewById(R.id.marcacochecliente)
        problema_coche=findViewById(R.id.problemacochecliente)
        antiguedad = Util.obtenerFechaActual()
        boton_crear=findViewById(R.id.agregarcliente)
         var id_cliente: String=""
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


        var lista_cliente = Util.obtenerListaCliente(database, this)
        boton_crear.setOnClickListener {
            if ((nombre_cliente.text.toString().isEmpty() || matricula_cliente.text.toString().isEmpty()
                        || telefono_cliente.text.toString().isEmpty()) || modelo_coche.text.toString().isEmpty() || marca_coche.text.toString().isEmpty() || problema_coche.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Rellene todos los campos ",
                    Toast.LENGTH_SHORT
                ).show()
            }else if(nombre_cliente.text.toString().length>20){
                Toast.makeText(this,"Nombre muy largo", Toast.LENGTH_SHORT).show()

            }else if(modelo_coche.text.toString().length>20){
                Toast.makeText(this,"Modelo muy largo", Toast.LENGTH_SHORT).show()

            }else if(marca_coche.text.toString().length>20) {
                Toast.makeText(this, "Marca muy larga", Toast.LENGTH_SHORT).show()

                //la matricula tiene que tener obligatoriamente 4 numeros y 3 letras
            }else if ( matricula_cliente.length() != 7 || !matricula_cliente.text.toString().matches(Regex("[0-9]{4}[A-Z]{3}"))){
                Toast.makeText(this, "Matricula incorrecta", Toast.LENGTH_SHORT).show()

            }else if(telefono_cliente.text.toString().length!=9 || !telefono_cliente.text.toString().matches(Regex("[0-9]{9}"))){
                Toast.makeText(this, "Telefono incorrecto", Toast.LENGTH_SHORT).show()

            }else if (Util.existeCliente(
                    lista_cliente,
                    matricula_cliente.text.toString()
                )
            ) {
                Toast.makeText(this, "Cliente con ese coche afectado ya existe", Toast.LENGTH_SHORT)
                    .show()
            }else {
                //editar cliente en la base de datos
                //Obtener el color seleccionado para mostrar en la vista
              id_cliente= intent.getStringExtra("id_cliente").toString()



                    Log.d("id_cliente",id_cliente)
                    db_ref.child("Motor").child("Cliente").get().addOnSuccessListener {
                        if(it.exists()){
                            for(cliente in it.children){
                                val cliente=cliente.getValue(Cliente::class.java)
                                if(cliente!=null){
                                    if(cliente.id_cliente==id_cliente){
                                        //editar cliente en la base de datos
                                        val cliente_editado=Cliente(
                                            nombre_cliente.text.toString(),
                                            matricula_cliente.text.toString(),
                                            telefono_cliente.text.toString().toInt(),
                                            marca_coche.text.toString(),
                                            modelo_coche.text.toString(),
                                            selectedColor.toString(),
                                            problema_coche.text.toString(),
                                            cliente.id_taller,
                                            cliente.id_cliente,
                                            cliente.url_foto_cliente,
                                        )
                                        Util.escribirCliente(database,id_cliente,cliente_editado)

                                          Toast.makeText(this, "Cliente editado con éxito", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }

                            }
                        }
                    }
                }
            }
        }

        Botoncolores=findViewById(R.id.openColorPickerButton)
        colorseleccionado=findViewById(R.id.colorseleccionado)
        Botoncolores.setOnClickListener {
            //Mostrar el color por defecto el color seleccionado
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