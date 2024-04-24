package es.fconache.neogamedb.models

import com.google.gson.annotations.SerializedName

data class VideojuegosModel(
    @SerializedName("name") val nombre: String,
    @SerializedName("released") val fechasalida: String,
    @SerializedName("background_image") val imagen: String,
    @SerializedName("metacritic") val nota: Int,
    // Agrega más campos según tus necesidades
)

data class ListaVideojuegos(
    @SerializedName("results") val results: List<VideojuegosModel>
)