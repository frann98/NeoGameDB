package es.fconache.neogamedb

import android.app.Application
import android.content.Context
import es.fconache.neogamedb.databases.VideojuegosDBH

class AppClass : Application() {

    // Constantes para la configuración de la base de datos
    companion object {
        const val BASE = "BASE_1"
        const val TABLA = "videojuegos"
        const val VERSION = 1
        lateinit var appContext: Context
        lateinit var DB: VideojuegosDBH
    }

    override fun onCreate() {
        super.onCreate()

        // Inicialización del contexto de la aplicación
        appContext = applicationContext

        // Inicialización de la base de datos
        DB = VideojuegosDBH()
    }

}
