package es.fconache.neogamedb.databases

import android.content.ContentValues
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import es.fconache.neogamedb.AppClass
import es.fconache.neogamedb.models.VideojuegosSerializable
import java.lang.Exception

// Administrador de base de datos para manejar operaciones CRUD en la tabla de videojuegos
class VideojuegosDBAdmin {

    // Método para insertar un nuevo videojuego en la base de datos
    fun create(videojuego: VideojuegosSerializable): Long {
        val con = AppClass.DB.writableDatabase
        val values = ContentValues()
        values.put("nombre", videojuego.nombre)
        values.put("estado", videojuego.estado)
        values.put("notapersonal", videojuego.notaPersonal)

        // Insertar el registro en la base de datos, ignorando conflictos si ya existe
        val resultado = con.insertWithOnConflict(AppClass.TABLA, null, values, SQLiteDatabase.CONFLICT_IGNORE)

        con.close()
        return resultado
    }

    //--------------------------------------------------------------------------------------------//

    // Método para leer todos los registros de la tabla de videojuegos y devolverlos como una lista mutable
    fun readAll(): MutableList<VideojuegosSerializable> {
        val lista = mutableListOf<VideojuegosSerializable>()
        val query = "SELECT * FROM ${AppClass.TABLA}"
        val con = AppClass.DB.readableDatabase

        // Obtener el número total de filas en la tabla
        val cantidad = DatabaseUtils.queryNumEntries(con, AppClass.TABLA)

        // Si hay registros en la tabla, proceder a leerlos
        if (cantidad > 0) {
            try {
                val cursor = con.rawQuery(query, null)

                // Iterar a través del cursor y agregar cada registro a la lista
                while (cursor.moveToNext()) {
                    val dato = VideojuegosSerializable(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)
                    )
                    lista.add(dato)
                }
                cursor.close()
            } catch (ex: Exception) {
                // Mostrar un mensaje de error en caso de excepción durante la lectura
                mostrarError("Error al leer todos los registros ${ex.message}")
            }
        }
        con.close()
        return lista
    }

    //--------------------------------------------------------------------------------------------//

    // Método privado para mostrar un Toast con un mensaje de error
    private fun mostrarError(s: String) {
        Toast.makeText(AppClass.appContext, s, Toast.LENGTH_SHORT).show()
    }

    //--------------------------------------------------------------------------------------------//

    // Método para borrar todos los registros de la tabla de videojuegos
    fun borrarTodo() {
        val query = "DELETE FROM ${AppClass.TABLA}"
        val con = AppClass.DB.writableDatabase
        try {
            con.execSQL(query)
        } catch (ex: Exception) {
            // Mostrar un mensaje de error en caso de fallo al borrar todos los registros
            mostrarError("Error al borrar todo " + ex.message)
        }
        con.close()
    }

    //--------------------------------------------------------------------------------------------//

    // Método para borrar un registro específico de la tabla de videojuegos por su ID
    fun borrar(id: Int) {
        val con = AppClass.DB.writableDatabase
        con.delete(AppClass.TABLA, "id=?", arrayOf(id.toString()))
        con.close()
    }

    //--------------------------------------------------------------------------------------------//

    // Método para actualizar un registro de videojuego existente en la base de datos
    fun update(videojuego: VideojuegosSerializable): Boolean {
        // Verificar si ya existe un videojuego con el mismo nombre pero diferente ID
        if (existeNombre(videojuego.id, videojuego.nombre)) {
            return false // Devolver false si el nombre está duplicado
        }

        // El nombre no está duplicado, proceder con la actualización
        val con = AppClass.DB.writableDatabase
        val values = ContentValues()
        values.put("nombre", videojuego.nombre)
        values.put("estado", videojuego.estado)
        values.put("notapersonal", videojuego.notaPersonal)

        // Actualizar el registro en la base de datos utilizando el ID del videojuego
        con.update(AppClass.TABLA, values, "id=?", arrayOf(videojuego.id.toString()))
        con.close()
        return true // Devolver true indicando que la actualización fue exitosa
    }

    // Método privado para verificar si existe un nombre de videojuego duplicado en la base de datos
    private fun existeNombre(id: Int, nombre: String): Boolean {
        val query = "SELECT count(*) FROM ${AppClass.TABLA} WHERE nombre=? AND id <> ?"
        val con = AppClass.DB.readableDatabase
        val cursor = con.rawQuery(query, arrayOf(nombre, id.toString()))
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        con.close()
        return total > 0 // Devolver true si existe un nombre duplicado, false si no
    }

    //--------------------------------------------------------------------------------------------//
}
