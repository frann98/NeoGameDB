package es.fconache.neogamedb.providers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideojuegosProvider {

    val retrofit2 = Retrofit.Builder().baseUrl("https://api.rawg.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    val apiClient: VideojuegosInterface = retrofit2.create(VideojuegosInterface::class.java)

}