package es.fconache.neogamedb.models

import com.google.gson.annotations.SerializedName

// Modelo de datos para representar un videojuego
data class VideojuegosModel(
    @SerializedName("name") val nombre: String,          // Nombre del videojuego
    @SerializedName("released") val fechasalida: String, // Fecha de salida del videojuego
    @SerializedName("background_image") val imagen: String, // URL de la imagen de fondo del videojuego
    @SerializedName("metacritic") val nota: Int          // Puntuación Metacritic del videojuego
    // Puedes agregar más campos según las necesidades
)

// Modelo de datos para representar una lista de videojuegos
data class ListaVideojuegos(
    @SerializedName("results") val results: List<VideojuegosModel> // Lista de videojuegos
)
