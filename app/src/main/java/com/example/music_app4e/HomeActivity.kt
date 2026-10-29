package com.example.music_app4e

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.music_app4e.model.ITunesResponse
import com.example.music_app4e.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var tvEstado : TextView
    private lateinit var imgAlbum : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvEstado = findViewById(R.id.tvEstado)
        imgAlbum = findViewById(R.id.imgAlbum)

        obtenerAlbum()

    }

    // MÉTODO PARA OBTENER LA RESPUESTA DEL API
    private fun obtenerAlbum(){

        RetrofitClient.api.obtenerAlbumGenero(

            genero = "kpop",
            pais = "MX",
            media = "music",
            entidad = "album",
            limite = 1

        ).enqueue(object : Callback<ITunesResponse>{

            // MÉTODO EN CASO DE OBTENER UNA RESPUESTA EXITOSA
            override fun onResponse(call: Call<ITunesResponse?>, response: Response<ITunesResponse?>) {

                // Variable que obtenga toda la respuesta del API
                val albumes = response.body()?.results

                if(!albumes.isNullOrEmpty()){

                    // Variable para mostrar un solo resultado
                    val album = albumes[0]

                    tvEstado.text = "Album: ${album.collectionName}" +
                                    "Artista: ${album.artistName}"

                    // Portada del álbum
                    Glide.with(this@HomeActivity)
                        .load(album.obtenerImagenGrande())
                        .into(imgAlbum)

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

}