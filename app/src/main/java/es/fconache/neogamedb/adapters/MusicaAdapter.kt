package es.fconache.neogamedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R

class MusicaAdapter(var lista: List<String>, var onCancionClick: (Int) -> Unit) :
    RecyclerView.Adapter<MusicaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.musica_layout, parent, false)
        return MusicaViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: MusicaViewHolder, position: Int) {
        holder.render(lista[position], onCancionClick)
        for (i in lista) {

        }
    }

}