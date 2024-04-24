package es.fconache.neogamedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.models.VideojuegosSerializable

class VideojuegosSerializableAdapter(
    var lista: MutableList<VideojuegosSerializable>,
    val borrarVideojuego: (Int) -> Unit,
    val actualizarVideojuego: (VideojuegosSerializable) -> Unit,
    val subirVideojuego: (VideojuegosSerializable) -> Unit,
) : RecyclerView.Adapter<VideojuegosSerializableViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): VideojuegosSerializableViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_serializable, parent, false)
        return VideojuegosSerializableViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: VideojuegosSerializableViewHolder, position: Int) {
        holder.render(lista[position], borrarVideojuego, actualizarVideojuego,subirVideojuego)
    }
}