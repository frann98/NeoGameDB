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

class ExplorarFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var LLBotonesD: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)

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
            webView.loadUrl("https://www.youtube.com/")
            LLBotonesD.setBackgroundColor(Color.parseColor("#FF0000"))
        }

        btnTwitch.setOnClickListener {
            webView.loadUrl("https://www.twitch.tv/")
            LLBotonesD.setBackgroundColor(Color.parseColor("#A870FE"))
        }

        btnWTP.setOnClickListener {
            webView.loadUrl("https://whatoplay.com")
            LLBotonesD.setBackgroundColor(Color.parseColor("#1DC34A"))
        }

    }
}