package es.fconache.neogamedb.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.fconache.neogamedb.databinding.VideojuegosLayoutBinding
import es.fconache.neogamedb.models.VideojuegosModel

// ViewHolder para manejar la vista de cada ítem en el RecyclerView
class VideojuegosViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    // Binding para acceder a los elementos del layout del ítem
    private val binding = VideojuegosLayoutBinding.bind(v)

    // Método para renderizar y actualizar los datos del ítem
    fun render(videojuegos: VideojuegosModel, onItemClick: (String, String, String, Int) -> Unit) {
        // Asignar el nombre del videojuego al TextView correspondiente
        binding.tvNombreR.text = videojuegos.nombre.trim()
        // Asignar la fecha de salida del videojuego al TextView correspondiente
        binding.tvFechasalida.text = videojuegos.fechasalida
        // Asignar la nota del videojuego al TextView correspondiente, agregando "/100"
        binding.tvNota.text = videojuegos.nota.toString() + "/100"
        // Cargar la imagen del videojuego en el ImageView utilizando la librería Glide
        Glide.with(binding.ivImagen.context).load(videojuegos.imagen).into(binding.ivImagen)

        // Configurar el listener para el ítem, llamando a la función onItemClick con los datos del videojuego
        itemView.setOnClickListener {
            onItemClick(
                videojuegos.imagen, videojuegos.nombre, videojuegos.fechasalida, videojuegos.nota
            )
        }
    }
}
