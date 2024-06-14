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
import es.fconache.neogamedb.interfaces.OnBackPressedListener

// Fragmento para mostrar noticias de diferentes plataformas de videojuegos
class NoticiasFragment : Fragment(), OnBackPressedListener {

    private lateinit var webView: WebView
    private lateinit var LLBotones: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_noticias, container, false)

        // Inicializar WebView
        webView = view.findViewById(R.id.webViewExplorar)
        webView.webViewClient =
            WebViewClient() // Esto evita que se abran páginas web en el navegador externo
        webView.settings.javaScriptEnabled = true // Habilitar JavaScript si es necesario
        webView.loadUrl("https://www.nintenderos.com/category/noticias/") // URL inicial de carga

        // Inyectar JavaScript y CSS para el modo oscuro (comentado por ahora)
        //webView.webViewClient = object : WebViewClient() {
        //    override fun onPageFinished(view: WebView?, url: String?) {
        //        super.onPageFinished(view, url)
        //        enableDarkMode(webView)
        //    }
        //}

        // Inicializar contenedor de botones
        LLBotones = view.findViewById(R.id.linear_layout_botonesD)

        // Establecer color de fondo inicial del LinearLayout
        LLBotones.setBackgroundColor(Color.parseColor("#FF0000"))

        return view
    }

    override fun onBackPressed(): Boolean {
        return if (webView.canGoBack()) {
            webView.goBack()
            true
        } else {
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar listeners de los botones
        setListeners()
    }

    // Función para activar el modo oscuro en el WebView mediante JavaScript
    private fun enableDarkMode(webView: WebView) {
        val js = """
            (function() {
                var style = document.createElement('style');
                style.innerHTML = `
                    * {
                        background-color: #121212 !important;
                        color: #ffffff !important;
                    }
                `;
                document.head.appendChild(style);
            })();
        """.trimIndent()

        // Evaluar el JavaScript en el WebView, compatible con versiones KitKat y superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null)
        } else {
            webView.loadUrl("javascript:$js")
        }
    }

    // Función para configurar los listeners de los botones
    private fun setListeners() {
        val btnNintendo: Button = requireView().findViewById(R.id.btnNintendo)
        val btnSony: Button = requireView().findViewById(R.id.btnSony)
        val btnXbox: Button = requireView().findViewById(R.id.btnXbox)
        val btnPC: Button = requireView().findViewById(R.id.btnPC)

        // Configurar acciones para cada botón
        btnNintendo.setOnClickListener {
            webView.loadUrl("https://www.nintenderos.com/category/noticias/")
            LLBotones.setBackgroundColor(Color.parseColor("#FF0000"))
        }

        btnSony.setOnClickListener {
            webView.loadUrl("https://blog.es.playstation.com")
            LLBotones.setBackgroundColor(Color.parseColor("#00439B"))
        }

        btnXbox.setOnClickListener {
            webView.loadUrl("https://www.somosxbox.com/noticias")
            LLBotones.setBackgroundColor(Color.parseColor("#157D35"))
        }

        btnPC.setOnClickListener {
            webView.loadUrl("https://www.muycomputer.com/noticias/")
            LLBotones.setBackgroundColor(Color.parseColor("#000000"))
        }
    }
}
