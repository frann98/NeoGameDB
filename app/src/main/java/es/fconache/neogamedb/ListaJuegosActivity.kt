package es.fconache.neogamedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.fconache.neogamedb.adapters.VideojuegosSerializableAdapter
import es.fconache.neogamedb.databases.VideojuegosDBAdmin
import es.fconache.neogamedb.databinding.ActivityListaJuegosBinding
import es.fconache.neogamedb.models.VideojuegosSerializable

class ListaJuegosActivity : AppCompatActivity() {

    private lateinit var fdb: FirebaseDatabase;
    private lateinit var dbr: DatabaseReference;

    private lateinit var binding: ActivityListaJuegosBinding
    private var lista = mutableListOf<VideojuegosSerializable>()
    private lateinit var adapter: VideojuegosSerializableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaJuegosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fdb = FirebaseDatabase.getInstance()
        dbr = fdb.getReference()

        setRecycler()

        title = "BD Videojuegos"
    }

    //--------------------------------------------------------------------------------------------//

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvVideojuegosS.layoutManager = layoutManager
        rellenarLista()
        adapter = VideojuegosSerializableAdapter(lista,
            { id -> borrar(id) },
            { actualizar(it) },
            { subir(it) })
        binding.rvVideojuegosS.adapter = adapter
    }

    //--------------------------------------------------------------------------------------------//

    private fun subir(it: VideojuegosSerializable) {

        val datos = listOf(
            "Titulo: " + (it.nombre),
            ("Estado: " + it.estado),
            ("Nota_Personal: " + it.notaPersonal.toString())
        )
        val datosM = mutableMapOf<String, Any>()
        datosM["videojuego"] = datos
        dbr.setValue(datosM)
        Toast.makeText(this, "Subido a la Base de datos", Toast.LENGTH_SHORT).show()

    }
    //--------------------------------------------------------------------------------------------//

    private fun actualizar(videojuego: VideojuegosSerializable) {
        val i = Intent(this, AgregarActivity::class.java)
        i.putExtra("VIDEOJUEGO", videojuego)
        startActivity(i)
    }

    //--------------------------------------------------------------------------------------------//
    private fun borrar(id: Int) {
        val ac = VideojuegosDBAdmin()
        ac.borrar(id)
        setRecycler()
    }

    //--------------------------------------------------------------------------------------------//

    private fun rellenarLista() {
        val ac = VideojuegosDBAdmin()
        lista = ac.readAll()
        if (lista.isEmpty()) {
            binding.ivVideojuegosS.visibility = View.VISIBLE
        } else {
            binding.ivVideojuegosS.visibility = View.INVISIBLE
        }
    }

    //--------------------------------------------------------------------------------------------//

    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }

    //--------------------------------------------------------------------------------------------//

    override fun onResume() {
        super.onResume()
        setRecycler()
    }

    //--------------------------------------------------------------------------------------------//

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //--------------------------------------------------------------------------------------------//

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_about -> {
                startActivity(Intent(this, AcercaDeActivity::class.java))
            }

            R.id.mi_listaJuegos -> {
                startActivity(Intent(this, ListaJuegosActivity::class.java))
            }

            R.id.mi_salir -> {
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}