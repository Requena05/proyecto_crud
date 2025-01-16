package com.example.proyecto_crud

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class AgregarCliente : AppCompatActivity() {
    private var selectedColor: Int = Color.WHITE // Color por defecto
    private lateinit var colorseleccionado: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_cliente)
        val openColorPickerButton: Button = findViewById(R.id.openColorPickerButton)
        openColorPickerButton.setOnClickListener {
            showColorPickerDialog()
        }
        colorseleccionado = findViewById(R.id.colorseleccionado)

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