package es.fconache.neogamedb.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import es.fconache.neogamedb.adapters.MusicaAdapter
import java.io.File
import java.lang.Exception

class MusicaFragment : Fragment() {

    //Necesitamos un fileDescriptor, en este caso, de Asset
    lateinit var fd: AssetFileDescriptor

    //Necesitamos también un MediaPlayer
    lateinit var mp: MediaPlayer

    var NombreCancionActual = ""

    var indiceCancionActual = 0

    // Creamos una lista con todas las canciones

    private val listaCanciones by lazy {
        val ficheros = this.activity?.assets?.list("")?.toList() ?: listOf<String>()
        ficheros.filter { it.contains(".mp3") }
    }

    //Para el recycler

    private lateinit var adapter: MusicaAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_musica, container, false)
        //

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitulo: TextView = requireView().findViewById(R.id.tvMusica)

        tvTitulo.visibility = View.INVISIBLE

        NombreCancionActual = listaCanciones[indiceCancionActual]

        //Iniciamos el file descriptor y el media player
        mp = MediaPlayer()
        fd = requireActivity().assets.openFd(NombreCancionActual)

        //Configuramos el mp para este fichero
        mp.setDataSource(
            fd.fileDescriptor, fd.startOffset, fd.length
        )
        fd.close()
        mp.prepare()
        setListeners()
        setRecycler()

    }

    private fun setRecycler() {
        val rvMusica: RecyclerView = requireView().findViewById(R.id.rvMusica)
        val layoutManager = LinearLayoutManager(this.context)
        rvMusica.layoutManager = layoutManager
        adapter = MusicaAdapter(listaCanciones) { indice -> ponerIndice(indice) }
        rvMusica.adapter = adapter
    }

    private fun setListeners() {

        val ibPlay: ImageButton = requireView().findViewById(R.id.ib_play)
        val ibParar: ImageButton = requireView().findViewById(R.id.ib_parar)
        val ibAnterior: ImageButton = requireView().findViewById(R.id.ib_anterior)
        val ibSiguiente: ImageButton = requireView().findViewById(R.id.ib_siguiente)
        val ibSelect: ImageButton = requireView().findViewById(R.id.ib_select)


        ibPlay.setOnClickListener {
            reproducir()
        }
        ibParar.setOnClickListener {
            parar()
        }
        ibAnterior.setOnClickListener {
            reproducirAnterior()
        }
        ibSiguiente.setOnClickListener {
            reproducirSiguiente()
        }
        ibSelect.setOnClickListener {
            abrirFileChooser()
        }
    }

    private fun abrirFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(Intent.createChooser(intent, "Seleccionar archivo"), 100)
        } catch (e: Exception) {
            Toast.makeText(this.context, "Por favor, instale FileManager", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //--------------------------------------------------------------------------------------------//

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri: Uri = data.data!!
            //
            val path: String = uri?.path.toString()
            val file = File(path)
            //
            Toast.makeText(this.context, "Path: $path File: ${file.name}", Toast.LENGTH_SHORT)
                .show()
            //
            playFile(uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun playFile(uri: Uri) {
        mp?.release()
        mp = MediaPlayer().apply {
            this@MusicaFragment.context?.let { setDataSource(it, uri) }
            prepare()
            start()
        }
        val path: String = uri?.path.toString()
        val file = File(path)
        val ibPlay: ImageButton = requireView().findViewById(R.id.ib_play)
        val tvTitulo: TextView = requireView().findViewById(R.id.tvMusica)
        tvTitulo.text = file.name
        tvTitulo.visibility = View.VISIBLE
        tvTitulo.animation = null
        crearAnimacion()
        if (mp.isPlaying) {
            mp.start()
            ibPlay.setImageResource(R.drawable.pause)
        } else {
            mp.pause()
            ibPlay.setImageResource(R.drawable.play)
        }
    }

    //--------------------------------------------------------------------------------------------//

    private fun ponerIndice(indice: Int) {
        indiceCancionActual = indice
        refrescarCancion()
    }

    //--------------------------------------------------------------------------------------------//

    private fun reproducirSiguiente() {
        if (indiceCancionActual == listaCanciones.size - 1) {
            indiceCancionActual = 0
        } else {
            indiceCancionActual++
        }
        refrescarCancion()
    }

    //--------------------------------------------------------------------------------------------//


    private fun reproducirAnterior() {
        if (indiceCancionActual == 0) {
            indiceCancionActual = listaCanciones.size - 1
        } else {
            indiceCancionActual--
        }
        refrescarCancion()
    }

    //--------------------------------------------------------------------------------------------//


    private fun refrescarCancion() {
        val tvTitulo: TextView = requireView().findViewById(R.id.tvMusica)
        val ibPlay: ImageButton = requireView().findViewById(R.id.ib_play)
        NombreCancionActual = listaCanciones[indiceCancionActual]

        fd = requireActivity().assets.openFd(NombreCancionActual)
        //Reseteamos el mediaPlayer
        mp.reset()
        //Configuramos el mp para este fichero
        mp.setDataSource(
            fd.fileDescriptor, fd.startOffset, fd.length
        )
        fd.close()
        mp.prepare()
        mp.start()
        tvTitulo.text = NombreCancionActual
        tvTitulo.animation = null
        crearAnimacion()
        tvTitulo.visibility = View.VISIBLE
        ibPlay.setImageResource(R.drawable.pause)
    }

    //--------------------------------------------------------------------------------------------//

    private fun parar() {
        val ibPlay: ImageButton = requireView().findViewById(R.id.ib_play)
        if (mp.isPlaying) {
            mp.pause()
        }
        mp.seekTo(0)
        ibPlay.setImageResource(R.drawable.play)
    }

    //--------------------------------------------------------------------------------------------//

    private fun reproducir() {
        val ibPlay: ImageButton = requireView().findViewById(R.id.ib_play)
        val tvTitulo: TextView = requireView().findViewById(R.id.tvMusica)
        tvTitulo.text = NombreCancionActual
        tvTitulo.visibility = View.VISIBLE
        tvTitulo.animation = null
        crearAnimacion()
        if (!mp.isPlaying) {
            mp.start()
            ibPlay.setImageResource(R.drawable.pause)
        } else {
            mp.pause()
            ibPlay.setImageResource(R.drawable.play)
        }

    }

    //--------------------------------------------------------------------------------------------//
    private fun crearAnimacion() {
        val tvTitulo: TextView = requireView().findViewById(R.id.tvMusica)
        val longitudTexto = tvTitulo.paint.measureText(NombreCancionActual)
        val animacion = TranslateAnimation(0f, -longitudTexto, 0f, 0f)
        animacion.duration = 5000
        animacion.interpolator = LinearInterpolator()
        animacion.repeatCount = TranslateAnimation.INFINITE
        animacion.repeatMode = TranslateAnimation.RESTART
        tvTitulo.animation = animacion
    }
}