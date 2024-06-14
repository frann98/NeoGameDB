package es.fconache.neogamedb.fragments

import android.graphics.Color
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
import es.fconache.neogamedb.interfaces.OnBackPressedListener
import es.fconache.neogamedb.models.VideojuegosSerializable
import kotlin.random.Random

// Fragmento para explorar información de videojuegos en la web
class ExplorarFragment : Fragment(), OnBackPressedListener {

    private lateinit var webView: WebView
    private lateinit var LLBotonesD: LinearLayout
    private var lista = mutableListOf<VideojuegosSerializable>()
    private var nombreJuego = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)

        // Inicializar WebView
        webView = view.findViewById(R.id.webViewExplorar)
        webView.webViewClient =
            WebViewClient() // Esto evita que se abran páginas web en el navegador externo
        webView.settings.javaScriptEnabled = true // Habilitar JavaScript si es necesario
        val nj = obtenerJuego().replace("-", "+")
        webView.loadUrl("https://www.youtube.com/results?search_query=" + nj)

        // Inyectar JavaScript y CSS para el modo oscuro
        //webView.webViewClient = object : WebViewClient() {
        //    override fun onPageFinished(view: WebView?, url: String?) {
        //        super.onPageFinished(view, url)
        //        enableDarkMode(webView)
        //    }
        //}

        // Inicializar contenedor de botones
        LLBotonesD = view.findViewById(R.id.linear_layout_botonesD)

        // Establecer color de fondo inicial del LinearLayout
        LLBotonesD.setBackgroundColor(Color.parseColor("#FF0000"))

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar listeners de los botones
        setListeners()
    }

    // Función para configurar los listeners de los botones
    private fun setListeners() {
        val btnYoutube: Button = requireView().findViewById(R.id.btnYoutube)
        val btnTwitch: Button = requireView().findViewById(R.id.btnTwitch)
        val btnWTP: Button = requireView().findViewById(R.id.btnWTP)

        btnYoutube.setOnClickListener {
            val nj = obtenerJuego().replace("-", "+")
            webView.loadUrl("https://www.youtube.com/results?search_query=" + nj)
            LLBotonesD.setBackgroundColor(Color.parseColor("#FF0000"))
        }

        btnTwitch.setOnClickListener {
            val nj = obtenerJuego()
            webView.loadUrl("https://www.twitch.tv/directory/category/" + nj)
            LLBotonesD.setBackgroundColor(Color.parseColor("#A870FE"))
        }

        btnWTP.setOnClickListener {
            val nj = obtenerJuego().replace("-", "%20")
            webView.loadUrl("https://whatoplay.com/search/?games%5Bquery%5D=" + nj)
            LLBotonesD.setBackgroundColor(Color.parseColor("#1DC34A"))
        }
    }

    // Función para obtener un nombre de juego aleatorio desde la base de datos
    private fun obtenerJuego(): String {
        val ac = VideojuegosDBAdmin()
        var nombreJuego = ""

        // Leer todos los videojuegos de la base de datos
        lista = ac.readAll()

        // Si no hay videojuegos en la base de datos, se selecciona "Minecraft" como nombre de juego por defecto
        if (lista.isEmpty()) {
            nombreJuego = "Minecraft"
        } else {
            // Se elige aleatoriamente un juego de la lista de videojuegos
            nombreJuego = lista[Random.nextInt(0, lista.size)].nombre
        }

        // Formatear el nombre del juego en minúsculas y reemplazar espacios por guiones para usar en URLs
        return nombreJuego.lowercase().replace(" ", "-")
    }

    override fun onBackPressed(): Boolean {
        return if (webView.canGoBack()) {
            webView.goBack()
            true
        } else {
            false
        }
    }
}
