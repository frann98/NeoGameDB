package es.fconache.neogamedb.shared

import android.content.Context

class Preferences(val c: Context) {

    val storage = c.getSharedPreferences("DARKMODE", 0)

    fun ponerModo(mo: Boolean) {
        storage.edit().putBoolean("MODO_OSCURO", mo).apply()
    }

    fun leerModo(): Boolean {

        return storage.getBoolean("MODO_OSCURO", false)

    }
}