package com.example.music_app4e

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    private lateinit var imgPortadaDetalle : ImageView
    private lateinit var tvNombreAlbumDetalle : TextView
    private lateinit var tvArtistaAlbumDetalle : TextView
    private lateinit var tvAlbumDetalle : TextView
    private lateinit var btnFavorito : ImageButton

    private lateinit var dbHelper : DatabaseHelper

    private var albumFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        tvNombreAlbumDetalle = findViewById(R.id.tvNombreAlbumDetalle)
        tvArtistaAlbumDetalle = findViewById(R.id.tvArtistaAlbumDetalle)
        tvAlbumDetalle = findViewById(R.id.tvAlbumDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)

        dbHelper = DatabaseHelper(this)

        mostrarInformacionAlbum()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // MÉTODO PARA OBTENER LA INFORMACIÓN QUE VIENE DEL API
    private fun mostrarInformacionAlbum(){

        val idAlbum = intent.getLongExtra("ID_ALBUM", 1)
        val nombreAlbum = intent.getStringExtra("NOMBRE_ALBUM") ?: "Album desconocido"
        val artista = intent.getStringExtra("ARTISTA_ALBUM") ?: "Artista desconocido"
        val genero = intent.getStringExtra("GENERO_ALBUM") ?: "Sin genero"
        val numeroCanciones = intent.getIntExtra("CANCIONES_ALBUM", 1)
        val imagen = intent.getStringExtra("IMAGEN_ALBUM") ?: ""

        tvNombreAlbumDetalle.text = nombreAlbum
        tvArtistaAlbumDetalle.text = artista
        tvAlbumDetalle.text = genero + " - " + "$numeroCanciones canciones"

        Glide.with(this)
            .load(imagen)
            .centerCrop()
            .into(imgPortadaDetalle)

        // Verificar que el álbum se encuentre como favorito
        if(idAlbum != -1L){
            dbHelper.existeAlbumFavorito(idAlbum)
        }

        btnFavorito.setOnClickListener {

            //albumFavorito = !albumFavorito

            if(albumFavorito){

                val eliminado = dbHelper.eliminarAlbumFavorito(idAlbum)

                if(eliminado){
                    albumFavorito = !albumFavorito
                    Toast.makeText(this, "Album $nombreAlbum eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    btnFavorito.setImageResource(R.drawable.no_favorito)
                }



            }else{

                val guardado = dbHelper.guardarAlbumFavorito(
                  idAlbum = idAlbum,
                    nombreAlbum = nombreAlbum,
                    artista = artista,
                    genero = genero,
                    canciones = numeroCanciones,
                    imagen = imagen

                )

                if(guardado){
                    albumFavorito = !albumFavorito
                    btnFavorito.setImageResource(R.drawable.favorito)
                    Toast.makeText(this, "Album $nombreAlbum agregado a favoritos", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Album $nombreAlbum ya está a favoritos", Toast.LENGTH_SHORT).show()

                }


            }

        }

    }

}