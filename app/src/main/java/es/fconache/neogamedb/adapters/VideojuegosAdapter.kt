package es.fconache.neogamedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.models.VideojuegosModel

// Adapter para el RecyclerView que muestra una lista de videojuegos
class VideojuegosAdapter(
    // Lista de objetos VideojuegosModel que se mostrarán en el RecyclerView
    var lista: List<VideojuegosModel>,
    // Función lambda que se ejecuta cuando se hace clic en un ítem, pasando varios parámetros
    private val onItemClick: (String, String, String, Int) -> Unit
) : RecyclerView.Adapter<VideojuegosViewHolder>() {

    // Método que se llama cuando el RecyclerView necesita un nuevo ViewHolder del tipo especificado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideojuegosViewHolder {
        // Inflar el layout del ítem de videojuegos y crear el ViewHolder correspondiente
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.videojuegos_layout, parent, false)
        return VideojuegosViewHolder(v)
    }

    // Método que devuelve la cantidad de ítems en la lista
    override fun getItemCount(): Int {
        return lista.size
    }

    // Método que se llama para actualizar el contenido de un ViewHolder existente con datos nuevos
    override fun onBindViewHolder(holder: VideojuegosViewHolder, position: Int) {
        // Llamar al método render del ViewHolder para actualizar su contenido
        holder.render(lista[position], onItemClick)
    }
}
