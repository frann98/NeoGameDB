package es.fconache.neogamedb.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.databinding.LayoutSerializableBinding
import es.fconache.neogamedb.models.VideojuegosSerializable

class VideojuegosSerializableViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = LayoutSerializableBinding.bind(v)
    fun render(
        videojuego: VideojuegosSerializable,
        borrarVideojuego: (Int) -> Unit,
        actualizarVideojuego: (VideojuegosSerializable) -> Unit,
        subirVideojuego: (VideojuegosSerializable) -> Unit
    ) {
        binding.tvNombreR.text = videojuego.nombre
        binding.tvEstadoR.text = videojuego.estado
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
        binding.tvNotaPersonalR.text = videojuego.notaPersonal.toString()

        binding.btnBorrar.setOnClickListener {
            borrarVideojuego(videojuego.id)
        }

        binding.btnUpdate.setOnClickListener {
            actualizarVideojuego(videojuego)
        }

        binding.btnSubir.setOnClickListener{
            subirVideojuego(videojuego)
        }
    }


}