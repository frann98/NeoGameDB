package es.fconache.neogamedb.shared

import android.content.Context

class Preferences(val c: Context) {

    // Obtiene una instancia de SharedPreferences con el nombre "DARKMODE"
    val storage = c.getSharedPreferences("DARKMODE", 0)

    // Método para almacenar el estado del modo oscuro
    fun ponerModo(mo: Boolean) {
        storage.edit().putBoolean("MODO_OSCURO", mo).apply()
    }

    // Método para leer el estado del modo oscuro
    fun leerModo(): Boolean {
        return storage.getBoolean("MODO_OSCURO", false)
    }
}
