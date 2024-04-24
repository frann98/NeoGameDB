package es.fconache.neogamedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.models.VideojuegosModel

class VideojuegosAdapter(
    var lista: List<VideojuegosModel>, private val onItemClick: (String,String,String,Int) -> Unit
) : RecyclerView.Adapter<VideojuegosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideojuegosViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.videojuegos_layout, parent, false)
        return VideojuegosViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: VideojuegosViewHolder, position: Int) {
        holder.render(lista[position],onItemClick)
    }
}