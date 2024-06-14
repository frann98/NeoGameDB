package es.fconache.neogamedb.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.DetalleActivity
import es.fconache.neogamedb.R
import es.fconache.neogamedb.adapters.VideojuegosAdapter
import es.fconache.neogamedb.viewmodels.VideojuegosViewModel

// Fragmento para mostrar una lista de videojuegos y permitir búsqueda
class VideojuegosFragment : Fragment() {

    // ViewModel asociado al fragmento
    private val videojuegosViewModel: VideojuegosViewModel by viewModels()

    lateinit var adapter: VideojuegosAdapter // Adaptador para RecyclerView
    var p0 = "" // Variable para almacenar la búsqueda actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observador de la lista de videojuegos en el ViewModel
        videojuegosViewModel.listaVideojuegos.observe(this@VideojuegosFragment.requireActivity()) {
            adapter.lista = it // Actualizar la lista del adaptador con los nuevos datos
            adapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        return inflater.inflate(R.layout.fragment_videojuegos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView y listeners
        setRecycler()
        setListeners()
    }

    // Configurar listeners de la interfaz de búsqueda
    private fun setListeners() {
        val svVideojuegos: SearchView = requireView().findViewById(R.id.svVideojuegos)

        svVideojuegos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    p0 = query.trim().lowercase()
                    if (query.isEmpty()) {
                        // Mostrar mensaje si la búsqueda está vacía
                        Toast.makeText(
                            this@VideojuegosFragment.context, "Búsqueda vacía", Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    // Realizar la búsqueda utilizando el ViewModel
                    videojuegosViewModel.obtenerPorBusqueda(p0)
                    return true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // Configurar RecyclerView y su adaptador
    private fun setRecycler() {
        val rvVideojuegos: RecyclerView = requireView().findViewById(R.id.rvVideojuegos)
        val layoutManager = LinearLayoutManager(this.context)
        rvVideojuegos.layoutManager = layoutManager
        // Inicializar el adaptador con una lista vacía y un listener para mostrar detalles
        adapter = VideojuegosAdapter(arrayListOf(),
            { imagen, nombre, salida, nota -> mostrarDetalle(imagen, nombre, salida, nota) })
        rvVideojuegos.adapter = adapter
    }

    // Función para mostrar los detalles de un videojuego en una actividad de detalle
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
