package es.fconache.neogamedb.fragments

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import es.fconache.neogamedb.R
import java.lang.Exception


class VideosFragment : Fragment() {

    private lateinit var mediaController: MediaController

    private var posicion = 0

    private var rutaVideo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaController = MediaController(this.context)
        setListeners()

    }

    //--------------------------------------------------------------------------------------------//

    private fun setListeners() {

        val btnPlay: Button = requireView().findViewById(R.id.btn_playv)

        btnPlay.setOnClickListener {
            reproducirVideo()
        }
    }

    //--------------------------------------------------------------------------------------------//

    private fun reproducirVideo() {
        val videoView: VideoView = requireView().findViewById(R.id.videoView)
        var aleatorio = (0..3).random()
        var idVideo = 0
        when (aleatorio) {

            0 -> {
                idVideo = R.raw.apm1
            }

            1 -> {
                idVideo = R.raw.apm2
            }

            2 -> {
                idVideo = R.raw.apm3
            }

            3 -> {
                idVideo = R.raw.apm4
            }

            else -> {
                idVideo = R.raw.apm1
            }

        }
        rutaVideo = "android.resource://${requireContext().packageName}/$idVideo"

        verVideo()

        if (posicion == 0) {
            videoView.seekTo(1)
        } else {
            videoView.seekTo(posicion)
        }
    }

    //--------------------------------------------------------------------------------------------//

    private fun verVideo() {
        val videoView: VideoView = requireView().findViewById(R.id.videoView)
        val uri = Uri.parse(rutaVideo)
        try {
            videoView.setVideoURI(uri)
            videoView.requestFocus()
            if (posicion != 0) {
                videoView.seekTo(posicion)
            }
            videoView.start()
        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
        }
        //Parte 2 con MediaController
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)
    }

    //--------------------------------------------------------------------------------------------//

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val videoView: VideoView = requireView().findViewById(R.id.videoView)
        outState.putInt("POSICION", videoView.currentPosition)
        outState.putString("VIDEO", rutaVideo)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            posicion = savedInstanceState.getInt("POSICION")
            rutaVideo = savedInstanceState.getString("VIDEO", "")
            verVideo()
        }

    }

    //--------------------------------------------------------------------------------------------//

    override fun onPause() {
        super.onPause()
        //Toast.makeText(this.context, "MP4 ON PAUSE", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        //Toast.makeText(this.context, "MP4 ON RESUME", Toast.LENGTH_SHORT).show()
    }


}