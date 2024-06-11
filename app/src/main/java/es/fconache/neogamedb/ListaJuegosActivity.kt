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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.fconache.neogamedb.adapters.VideojuegosSerializableAdapter
import es.fconache.neogamedb.databases.VideojuegosDBAdmin
import es.fconache.neogamedb.databinding.ActivityListaJuegosBinding
import es.fconache.neogamedb.models.VideojuegosSerializable

class ListaJuegosActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fdb: FirebaseDatabase;
    private lateinit var dbr: DatabaseReference;

    private lateinit var binding: ActivityListaJuegosBinding
    private var lista = mutableListOf<VideojuegosSerializable>()
    private lateinit var adapter: VideojuegosSerializableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaJuegosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        fdb = FirebaseDatabase.getInstance()
        dbr = fdb.getReference()

        setRecycler()
        binding.botonFlotante.setOnClickListener() {
            fetchEntriesFromFirebase()
        }

        title = "BD Videojuegos"
    }

    private fun fetchEntriesFromFirebase() {

        val user = auth.currentUser

        user?.let {
            val userEntriesRef = fdb.reference.child("users").child(it.uid).child("entries")

            userEntriesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val ac = VideojuegosDBAdmin()
                    val listavj = ac.readAll()

                    if (listavj.size!=0){
                        ac.borrarTodo()
                    }

                    for (entrySnapshot in snapshot.children) {
                        val id = entrySnapshot.child("id").getValue().toString()
                        val titulo = entrySnapshot.child("titulo").getValue().toString()
                        val estado = entrySnapshot.child("estado").getValue().toString()
                        val notapersonal = entrySnapshot.child("notapersonal").getValue().toString()

                        val videojuego = VideojuegosSerializable(
                            id.toInt(), titulo, estado, notapersonal.toInt()
                        )

                        //if (listavj.contains(videojuego)) {
                        //    break
                        //}

                        for (item in listavj){
                            if (item.nombre==videojuego.nombre){
                                break
                            }
                        }

                        ac.create(videojuego)

                        //lista.add(videojuego)
                        setRecycler()
                    }

                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ListaJuegosActivity, "Error al recuperar juegos", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
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

        val user = auth.currentUser

        val id = it.id
        val titulo = it.nombre
        val estado = it.estado
        val notapersonal = it.notaPersonal

        val entry = hashMapOf<String, Any>(
            "id" to id, "titulo" to titulo, "estado" to estado, "notapersonal" to notapersonal
        )

        user?.let {
            val userEntriesRef = fdb.reference.child("users").child(it.uid).child("entries")
            val newEntryRef = userEntriesRef.push() //Clave unica por operacion
            newEntryRef.setValue(entry)
        }
        Toast.makeText(this, "Subido a la BBDD", Toast.LENGTH_SHORT).show()
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