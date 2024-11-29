package com.example.proyecto_crud


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class Utile {
    companion object{
        fun existetaller(taller: List<Taller>, nombre: String): Boolean {
            return taller.any { it.nombre!!.lowercase() == nombre.lowercase() }
        }
        fun obtenerListaTaller(db_ref: DatabaseReference, contexto: Context): MutableList<Taller> {
            val lista_taller = mutableListOf<Taller>()

            db_ref.child("nba").child("talleres")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { taller ->
                            val Taller_actual = taller.getValue(Taller::class.java)
                            lista_taller.add(Taller_actual!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(contexto, "Error al obtener los TALLERES", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
            return lista_taller
        }
        fun escribirTaller(db_ref: DatabaseReference,id: String, club: Taller) {
            db_ref.child("nba").child("talleres").child(id).setValue(club)
        }

        //LO CAMBIAREMOS
        suspend fun guardarLogo(almacen: StorageReference, id: String, Logo: Uri): String {
            var urlAlmacen: Uri
            urlAlmacen =
                almacen.child("Logos").child(id).putFile(Logo).await()
                    .storage.downloadUrl.await()

            return urlAlmacen.toString()
        }

        fun tostadaCorrutina(activity: AppCompatActivity, contexto: Context, texto: String){
            activity.runOnUiThread{
                Toast.makeText(contexto,texto,Toast.LENGTH_SHORT).show()
            }
        }

        fun animacion_carga(contexto: Context): CircularProgressDrawable {
            val animacion = CircularProgressDrawable(contexto)
            animacion.strokeWidth = 5f
            animacion.centerRadius = 30f
            animacion.start()

            return animacion
        }



    }
}