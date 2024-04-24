package es.fconache.neogamedb.databases

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.fconache.neogamedb.AppClass

class VideojuegosDBH :
    SQLiteOpenHelper(AppClass.appContext, AppClass.BASE, null, AppClass.VERSION) {

    private val createTable =
        "CREATE TABLE ${AppClass.TABLA}(id INTEGER PRIMARY KEY AUTOINCREMENT," + "nombre TEXT UNIQUE NOT NULL," + "estado TEXT NOT NULL," + "notapersonal INTEGER NOT NULL);"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE ${AppClass.TABLA}"
        db?.execSQL(dropTable)
        onCreate(db)
    }
}