package com.example.proyecto_crud

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await


class AgregarCliente : AppCompatActivity() {
    private var selectedColor: Int = Color.WHITE // Color por defecto
    private lateinit var tallerseleccionado: Spinner

    private lateinit var colorseleccionado: ImageView
    private lateinit var Botoncolores:AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_cliente)
        tallerseleccionado=findViewById(R.id.tallerseleccionado)
        val db_ref=FirebaseDatabase.getInstance().reference



        var lista_nombretaller: MutableList<String>
        lista_nombretaller=Util.obtenernombreTaller(db_ref,this)

        tallerseleccionado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_nombretaller)




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