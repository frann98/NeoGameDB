package es.fconache.neogamedb.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import es.fconache.neogamedb.R
import es.fconache.neogamedb.databases.VideojuegosDBAdmin
import es.fconache.neogamedb.models.VideojuegosSerializable
import kotlin.random.Random

class ExplorarFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var LLBotonesD: LinearLayout
    private var lista = mutableListOf<VideojuegosSerializable>()
    private var nombreJuego = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)

        // Initialize WebView
        webView = view.findViewById(R.id.webViewExplorar)
        webView.webViewClient = WebViewClient() // This prevents opening web pages in the browser
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        val nj = obtenerJuego().replace("-","+")
        webView.loadUrl("https://www.youtube.com/results?search_query="+nj)

        // Inject JavaScript and CSS for dark mode
        //webView.webViewClient = object : WebViewClient() {
        //override fun onPageFinished(view: WebView?, url: String?) {
        //super.onPageFinished(view, url)
        //enableDarkMode(webView)
        //}
        //}

        // Initialize Button Container
        LLBotonesD = view.findViewById(R.id.linear_layout_botonesD)

        // Set initial background color of the LinearLayout
        LLBotonesD.setBackgroundColor(Color.parseColor("#FF0000"))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

    }

    private fun setListeners() {
        val btnYoutube: Button = requireView().findViewById(R.id.btnYoutube)
        val btnTwitch: Button = requireView().findViewById(R.id.btnTwitch)
        val btnWTP: Button = requireView().findViewById(R.id.btnWTP)

        btnYoutube.setOnClickListener {
            val nj = obtenerJuego().replace("-","+")
            webView.loadUrl("https://www.youtube.com/results?search_query="+nj)
            LLBotonesD.setBackgroundColor(Color.parseColor("#FF0000"))
        }

        btnTwitch.setOnClickListener {
            val nj = obtenerJuego()
            webView.loadUrl("https://www.twitch.tv/directory/category/"+nj)
            LLBotonesD.setBackgroundColor(Color.parseColor("#A870FE"))
        }

        btnWTP.setOnClickListener {
            val nj = obtenerJuego().replace("-","%20")
            webView.loadUrl("https://whatoplay.com/search/?games%5Bquery%5D="+nj)
            LLBotonesD.setBackgroundColor(Color.parseColor("#1DC34A"))
        }

    }

    private fun obtenerJuego(): String {

        val ac = VideojuegosDBAdmin()
        lista = ac.readAll()
        nombreJuego = lista[Random.nextInt(0, lista.size)].nombre

        return nombreJuego.lowercase().replace(" ", "-")
    }
}