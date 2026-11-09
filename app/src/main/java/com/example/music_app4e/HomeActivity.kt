package com.example.music_app4e

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.music_app4e.model.Album
import com.example.music_app4e.model.ITunesResponse
import com.example.music_app4e.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var tvEstado : TextView
    private lateinit var contenedorIndie : LinearLayout
    private lateinit var contenedorPop : LinearLayout
    private lateinit var contenedorTOP : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvEstado = findViewById(R.id.tvEstado)
        contenedorIndie = findViewById(R.id.contenedorIndie)
        contenedorPop = findViewById(R.id.contenedorPop)
        contenedorTOP = findViewById(R.id.contenedorTOP)

        obtenerAlbum("indie", contenedorIndie)
        obtenerAlbum("pop", contenedorPop)
        obtenerAlbum("twenty one pilots", contenedorTOP)

    }

    // MÉTODO PARA OBTENER LA RESPUESTA DEL API
    private fun obtenerAlbum(genero : String, contenedor: LinearLayout){

        RetrofitClient.api.obtenerAlbumGenero(

            genero = genero,
            pais = "MX",
            media = "music",
            entidad = "album",
            limite = 15

        ).enqueue(object : Callback<ITunesResponse>{

            // MÉTODO EN CASO DE OBTENER UNA RESPUESTA EXITOSA
            override fun onResponse(call: Call<ITunesResponse?>, response: Response<ITunesResponse?>) {

                // Variable que obtenga toda la respuesta del API
                val albumes = response.body()?.results ?: emptyList()

                if(albumes.isNotEmpty()){

                    mostrarAlbumes(albumes, contenedor)

                }else{
                    Toast.makeText(this@HomeActivity, "No hay resultados", Toast.LENGTH_SHORT).show()
                }

            }

            // MÉTODO EN CASO DE UNA RESPUESTA FALLIDA
            override fun onFailure(call: Call<ITunesResponse?>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error en el API", Toast.LENGTH_SHORT).show()
            }

        })

    }

    // MÉTODO PARA MOSTRAR UNA LISTA DE ÁLBUM
    private fun mostrarAlbumes( albumes : List<Album>, contenedor : LinearLayout ){

        // Eliminar los elementos duplicados
        contenedor.removeAllViews()

        // Ciclo que recorre los resultados del álbum
        for(album in albumes){

            // Crear variable que haga uso de las tarjetas (item_album)
            val vistaAlbum = layoutInflater.inflate(
                R.layout.item_album,
                contenedor,
                false
            )

            // Obtener la información del API
            val imgPortada = vistaAlbum.findViewById<ImageView>(R.id.imgPortadaAlbum)
            val tvNombreAlbum = vistaAlbum.findViewById<TextView>(R.id.tvNombreAlbum)
            val tvNombreArtista = vistaAlbum.findViewById<TextView>(R.id.tvNombreArtista)

            tvNombreAlbum.text = album.collectionName ?: "Album sin nombre"
            tvNombreArtista.text = album.artistName ?: "Artista desconocido"

            Glide.with(this)
                .load(album.obtenerImagenGrande())
                .into(imgPortada)

            contenedor.addView(vistaAlbum)

        }

    }

}