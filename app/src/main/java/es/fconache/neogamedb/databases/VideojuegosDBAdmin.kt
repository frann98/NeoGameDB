package es.fconache.neogamedb.databases

import android.content.ContentValues
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import es.fconache.neogamedb.AppClass
import es.fconache.neogamedb.models.VideojuegosSerializable
import java.lang.Exception

class VideojuegosDBAdmin {


    fun create(videojuego: VideojuegosSerializable): Long {
        val con = AppClass.DB.writableDatabase
        val values = ContentValues()
        values.put("nombre", videojuego.nombre)
        values.put("estado", videojuego.estado)
        values.put("notapersonal", videojuego.notaPersonal)
        val resultado =
            con.insertWithOnConflict(AppClass.TABLA, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        con.close()
        return resultado;
    }

    //--------------------------------------------------------------------------------------------//

    fun readAll(): MutableList<VideojuegosSerializable> {

        val lista = mutableListOf<VideojuegosSerializable>()
        val query = "SELECT * FROM ${AppClass.TABLA}"
        val con = AppClass.DB.readableDatabase
        val cantidad = DatabaseUtils.queryNumEntries(
            con, AppClass.TABLA
        ) //devuelve el numero de filas de la tabla
        if (cantidad > 0) {
            try {
                val cursor = con.rawQuery(query, null)
                while (cursor.moveToNext()) {
                    val dato = VideojuegosSerializable(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)
                    )
                    lista.add(dato)
                }

            } catch (ex: Exception) {
                mostrarError("Error al leer todos los registros ${ex.message}")
            }
        }
        con.close()
        return lista
    }

    //--------------------------------------------------------------------------------------------//

    private fun mostrarError(s: String) {
        Toast.makeText(AppClass.appContext, s, Toast.LENGTH_SHORT).show()
    }

    //--------------------------------------------------------------------------------------------//

    fun borrarTodo() {
        val query = "DELETE FROM ${AppClass.TABLA}"
        val con = AppClass.DB.writableDatabase
        try {
            con.execSQL(query)
        } catch (ex: Exception) {
            mostrarError("Error al borrar todo " + ex.message)
        }
        con.close()
    }

    //--------------------------------------------------------------------------------------------//

    fun borrar(id: Int) {
        //val query="DELETE FROM ${Aplicacion.TABLA} WHERE id=$id" no es seguro antes SQL-Injection
        val con = AppClass.DB.writableDatabase
        con.delete(AppClass.TABLA, "id=?", arrayOf(id.toString()))
        con.close()
    }

    //--------------------------------------------------------------------------------------------//

    fun update(videojuego: VideojuegosSerializable): Boolean {

        if (existeNombre(videojuego.id, videojuego.nombre)) {
            return false
        }
        // El nombre NO está duplicado, actualizamos
        val con = AppClass.DB.writableDatabase
        val values = ContentValues()
        values.put("nombre", videojuego.nombre)
        values.put("estado", videojuego.estado)
        values.put("notapersonal", videojuego.notaPersonal)
        con.update(AppClass.TABLA, values, "id=?", arrayOf(videojuego.id.toString()))
        con.close()
        return true
    }

    private fun existeNombre(id: Int, nombre: String): Boolean {

        val query = "SELECT count(*) FROM ${AppClass.TABLA} WHERE nombre=? AND id <> ?"
        val con = AppClass.DB.readableDatabase
        val cursor = con.rawQuery(query, arrayOf(nombre, id.toString()))
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()
        con.close()
        return total > 0

    }

    //--------------------------------------------------------------------------------------------//
}