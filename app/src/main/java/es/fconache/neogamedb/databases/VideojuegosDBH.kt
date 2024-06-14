package es.fconache.neogamedb.databases

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.fconache.neogamedb.AppClass

// Clase que gestiona la creación y actualización de la base de datos SQLite para los videojuegos
class VideojuegosDBH :
    SQLiteOpenHelper(AppClass.appContext, AppClass.BASE, null, AppClass.VERSION) {

    // Sentencia SQL para crear la tabla de videojuegos
    private val createTable =
        "CREATE TABLE ${AppClass.TABLA}(id INTEGER PRIMARY KEY AUTOINCREMENT," + "nombre TEXT UNIQUE NOT NULL," + "estado TEXT NOT NULL," + "notapersonal INTEGER NOT NULL);"

    // Método llamado cuando se crea la base de datos por primera vez
    override fun onCreate(db: SQLiteDatabase?) {
        // Ejecutar la sentencia SQL para crear la tabla de videojuegos
        db?.execSQL(createTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos a una nueva versión
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Sentencia SQL para eliminar la tabla existente si hay una versión anterior
        val dropTable = "DROP TABLE ${AppClass.TABLA}"
        // Ejecutar la sentencia SQL para eliminar la tabla
        db?.execSQL(dropTable)
        // Llamar al método onCreate para crear la nueva versión de la tabla
        onCreate(db)
    }
}
