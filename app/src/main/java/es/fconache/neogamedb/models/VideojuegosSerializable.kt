package es.fconache.neogamedb.models

import java.io.Serializable

// Clase para representar un videojuego serializable
class VideojuegosSerializable(
    val id: Int,               // Identificador único del videojuego
    val nombre: String,        // Nombre del videojuego
    val estado: String,        // Estado actual del videojuego
    val notaPersonal: Int      // Nota personal del videojuego
) : Serializable             // Implementa la interfaz Serializable para permitir la serialización
