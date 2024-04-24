package es.fconache.neogamedb.models

import java.io.Serializable

class VideojuegosSerializable(
    val id: Int, val nombre: String, val estado: String, val notaPersonal: Int
) : Serializable