package es.fconache.neogamedb.providers

import es.fconache.neogamedb.models.ListaVideojuegos
import retrofit2.http.GET
import retrofit2.http.Query

interface VideojuegosInterface {

    // Método GET para obtener videojuegos desde la API
    @GET("api/games")
    suspend fun getVideojuegos(
        @Query("key") key: String, @Query("search") query: String
    ): ListaVideojuegos

}
