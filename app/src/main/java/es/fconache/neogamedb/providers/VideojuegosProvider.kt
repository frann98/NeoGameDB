package es.fconache.neogamedb.providers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideojuegosProvider {

    // Configuración de Retrofit con la URL de la API
    val retrofit2 =
        Retrofit.Builder().baseUrl("https://api.rawg.io/")  // URL de RawG para proveer de juegos
            .addConverterFactory(GsonConverterFactory.create())  // Usando Gson para la conversión de JSON
            .build()

    // Creación de la instancia del servicio VideojuegosInterface a partir de Retrofit
    val apiClient: VideojuegosInterface = retrofit2.create(VideojuegosInterface::class.java)

}
