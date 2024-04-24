package es.fconache.neogamedb.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.fconache.neogamedb.databinding.VideojuegosLayoutBinding
import es.fconache.neogamedb.models.VideojuegosModel

class VideojuegosViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val binding = VideojuegosLayoutBinding.bind(v)

    fun render(videojuegos: VideojuegosModel, onItemClick: (String, String, String, Int) -> Unit) {
        binding.tvNombreR.text = videojuegos.nombre.trim()
        binding.tvFechasalida.text = videojuegos.fechasalida
        binding.tvNota.text = videojuegos.nota.toString()+"/100"
        Glide.with(binding.ivImagen.context).load(videojuegos.imagen).into(binding.ivImagen)
        itemView.setOnClickListener{
            onItemClick(videojuegos.imagen,videojuegos.nombre,videojuegos.fechasalida,videojuegos.nota)
        }
    }

}