package es.fconache.neogamedb.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.DetalleActivity
import es.fconache.neogamedb.R
import es.fconache.neogamedb.adapters.VideojuegosAdapter
import es.fconache.neogamedb.viewmodels.VideojuegosViewModel

class VideojuegosFragment : Fragment() {

    private val videojuegosViewModel: VideojuegosViewModel by viewModels()

    lateinit var adapter: VideojuegosAdapter

    var p0 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        videojuegosViewModel.listaVideojuegos.observe(this@VideojuegosFragment.requireActivity()) {
            adapter.lista = it
            adapter.notifyDataSetChanged()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videojuegos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecycler()
        setListeners()

    }

    private fun setListeners() {
        val svVideojuegos: SearchView = requireView().findViewById(R.id.svVideojuegos)

        svVideojuegos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    p0 = query.trim().lowercase()
                    if (query.isEmpty()) {

                        Toast.makeText(
                            this@VideojuegosFragment.context, "Busqueda vacia", Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    println(
                        "LA QUERY HA SIDO: " + videojuegosViewModel.obtenerPorBusqueda(p0)
                            .toString()
                    )
                    videojuegosViewModel.obtenerPorBusqueda(p0)
                    //traerVideojuegos()
                    return true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun setRecycler() {
        val rvVideojuegos: RecyclerView = requireView().findViewById(R.id.rvVideojuegos)
        val layoutManager = LinearLayoutManager(this.context)
        rvVideojuegos.layoutManager = layoutManager
        adapter = VideojuegosAdapter(arrayListOf(),
            { imagen, nombre, salida, nota -> mostrarDetalle(imagen, nombre, salida, nota) })
        rvVideojuegos.adapter = adapter
    }

    private fun mostrarDetalle(imagen: String, nombre: String, salida: String, nota: Int) {

        val i = Intent(this.context, DetalleActivity::class.java).apply {
            putExtra("IMAGEN", imagen)
            putExtra("NOMBRE", nombre)
            putExtra("SALIDA", salida)
            putExtra("NOTA", nota)
        }
        startActivity(i)

    }

}