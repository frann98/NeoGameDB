package es.fconache.neogamedb.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.models.VideojuegosSerializable

// Adapter para el RecyclerView que maneja una lista de videojuegos serializables
class VideojuegosSerializableAdapter(
    // Lista mutable de objetos VideojuegosSerializable que se mostrarán en el RecyclerView
    var lista: MutableList<VideojuegosSerializable>,
    // Función lambda que se ejecuta cuando se desea borrar un videojuego, pasando la posición del ítem
    val borrarVideojuego: (Int) -> Unit,
    // Función lambda que se ejecuta cuando se desea actualizar un videojuego, pasando el objeto a actualizar
    val actualizarVideojuego: (VideojuegosSerializable) -> Unit,
    // Función lambda que se ejecuta cuando se desea subir un videojuego, pasando el objeto a subir
    val subirVideojuego: (VideojuegosSerializable) -> Unit,
) : RecyclerView.Adapter<VideojuegosSerializableViewHolder>() {

    // Método que se llama cuando el RecyclerView necesita un nuevo ViewHolder del tipo especificado
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): VideojuegosSerializableViewHolder {
        // Inflar el layout del ítem serializable y crear el ViewHolder correspondiente
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_serializable, parent, false)
        return VideojuegosSerializableViewHolder(v)
    }

    // Método que devuelve la cantidad de ítems en la lista
    override fun getItemCount(): Int {
        return lista.size
    }

    // Método que se llama para actualizar el contenido de un ViewHolder existente con datos nuevos
    override fun onBindViewHolder(holder: VideojuegosSerializableViewHolder, position: Int) {
        // Llamar al método render del ViewHolder para actualizar su contenido
        holder.render(lista[position], borrarVideojuego, actualizarVideojuego, subirVideojuego)
    }
}
