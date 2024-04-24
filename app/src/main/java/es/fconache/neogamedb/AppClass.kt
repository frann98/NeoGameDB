package es.fconache.neogamedb

import android.app.Application
import android.content.Context
import es.fconache.neogamedb.databases.VideojuegosDBH

class AppClass : Application() {
    companion object {
        const val BASE = "BASE_1"
        const val TABLA = "videojuegos"
        const val VERSION = 1
        lateinit var appContext: Context
        lateinit var DB: VideojuegosDBH
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        DB = VideojuegosDBH()
    }

}
