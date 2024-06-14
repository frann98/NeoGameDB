package es.fconache.neogamedb.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.databinding.LayoutSerializableBinding
import es.fconache.neogamedb.models.VideojuegosSerializable

// ViewHolder para manejar la vista de cada ítem en el RecyclerView
class VideojuegosSerializableViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    // Binding para acceder a los elementos del layout del ítem
    private val binding = LayoutSerializableBinding.bind(v)

    // Método para renderizar y actualizar los datos del ítem
    fun render(
        videojuego: VideojuegosSerializable,
        borrarVideojuego: (Int) -> Unit,
        actualizarVideojuego: (VideojuegosSerializable) -> Unit,
        subirVideojuego: (VideojuegosSerializable) -> Unit
    ) {
        // Asignar el nombre y el estado del videojuego a los TextViews correspondientes
        binding.tvNombreR.text = videojuego.nombre
        binding.tvEstadoR.text = videojuego.estado

        // Establecer la imagen según el estado del videojuego
        when (videojuego.estado) {
            "Pasado" -> {
                binding.ivSerializable.setImageResource(R.drawable.completado)
            }

            "Jugando" -> {
                binding.ivSerializable.setImageResource(R.drawable.jugando)
            }

            "Deseado" -> {
                binding.ivSerializable.setImageResource(R.drawable.deseado)
            }
        }

        // Asignar la nota personal del videojuego al TextView correspondiente
        binding.tvNotaPersonalR.text = videojuego.notaPersonal.toString()

        // Configurar el listener para el botón de borrar, llamando a la función borrarVideojuego con el ID del videojuego
        binding.btnBorrar.setOnClickListener {
            borrarVideojuego(videojuego.id)
        }

        // Configurar el listener para el botón de actualizar, llamando a la función actualizarVideojuego con el objeto del videojuego
        binding.btnUpdate.setOnClickListener {
            actualizarVideojuego(videojuego)
        }

        // Configurar el listener para el botón de subir, llamando a la función subirVideojuego con el objeto del videojuego
        binding.btnSubir.setOnClickListener {
            subirVideojuego(videojuego)
        }
    }
}
