package com.example.music_app4e.network

import com.example.music_app4e.model.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

// ARCHIVO PARA CONEXIÓN CON INTERNET

interface ITunesApi {

    @GET("search")

    // TODO: Agregar parámetros del enlace
    // MÉTODO PARA CREAR EL ENLACE DEL API
    fun obtenerAlbumGenero(
        @Query("term") genero : String,
        @Query("country") pais : String,
        @Query("media") media : String,
        @Query("entity") entidad : String,
        @Query("limit") limite : Int,
    ) : retrofit2.Call<ITunesResponse>

}