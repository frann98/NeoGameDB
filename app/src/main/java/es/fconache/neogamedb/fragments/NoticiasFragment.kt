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

class NoticiasFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var LLBotones: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_noticias, container, false)

        // Initialize WebView
        webView = view.findViewById(R.id.webViewExplorar)
        webView.webViewClient = WebViewClient() // This prevents opening web pages in the browser
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.loadUrl("https://www.nintenderos.com/category/noticias/") // Replace with your URL

        // Inject JavaScript and CSS for dark mode
        //webView.webViewClient = object : WebViewClient() {
            //override fun onPageFinished(view: WebView?, url: String?) {
                //super.onPageFinished(view, url)
                //enableDarkMode(webView)
            //}
        //}

        // Initialize Button Container
        LLBotones = view.findViewById(R.id.linear_layout_botonesD)

        // Set initial background color of the LinearLayout
        LLBotones.setBackgroundColor(Color.parseColor("#FF0000"))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

    }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null)
        } else {
            webView.loadUrl("javascript:$js")
        }
    }

    private fun setListeners() {
        val btnNintendo: Button = requireView().findViewById(R.id.btnYoutube)
        val btnSony: Button = requireView().findViewById(R.id.btnTwitch)
        val btnXbox: Button = requireView().findViewById(R.id.btnWTP)
        val btnPC: Button = requireView().findViewById(R.id.btnPC)

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