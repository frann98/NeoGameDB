package es.fconache.neogamedb.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.databinding.MusicaLayoutBinding

class MusicaViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var binding = MusicaLayoutBinding.bind(v)

    fun render(cancion: String, onCancionClick: (Int) -> Unit) {
        binding.tvTituloCancion.text = cancion
        itemView.setOnClickListener {
            onCancionClick(adapterPosition)
            itemView.setBackgroundColor(binding.ivNotaMusical.context.getColor(R.color.light_gray))
        }
    }

}