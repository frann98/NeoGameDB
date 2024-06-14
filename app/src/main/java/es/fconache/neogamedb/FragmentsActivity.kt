package es.fconache.neogamedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.fconache.neogamedb.databinding.ActivityFragmentsBinding
import es.fconache.neogamedb.fragments.ExplorarFragment
import es.fconache.neogamedb.fragments.NoticiasFragment
import es.fconache.neogamedb.fragments.VideojuegosFragment
import es.fconache.neogamedb.interfaces.OnBackPressedListener

class FragmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentsBinding

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "NeoGameDB" // Establece el título de la actividad en la barra de acción
        auth = Firebase.auth // Inicializa Firebase Authentication

        // Handle the back press for the activity
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragContPrincipal)

                if (fragment is OnBackPressedListener && fragment.onBackPressed()) {
                    // The fragment handled the back press
                } else {
                    // Default back press behavior
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        pintarEmail() // Muestra el email del usuario autenticado
        setListeners() // Configura los listeners para los botones en la interfaz
        cargarFragments() // Carga el fragmento inicial (VideojuegosFragment) al iniciar la actividad
    }

    private fun setListeners() {
        // Listener para el botón de cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            Firebase.auth.signOut() // Cierra la sesión del usuario
            finish() // Cierra esta actividad
        }

        // Listener para el botón de noticias
        binding.btnNoticias.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.fragContPrincipal, NoticiasFragment()
                ) // Reemplaza el fragmento actual por NoticiasFragment
            }
        }

        // Listener para el botón de videojuegos
        binding.btnVideojuegos.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.fragContPrincipal, VideojuegosFragment()
                ) // Reemplaza el fragmento actual por VideojuegosFragment
            }
        }

        // Listener para el botón de explorar
        binding.btnExplorar.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.fragContPrincipal, ExplorarFragment()
                ) // Reemplaza el fragmento actual por ExplorarFragment
            }
        }
    }

    private fun pintarEmail() {
        binding.tvEmail.text =
            auth.currentUser?.email.toString() // Muestra el email del usuario autenticado en un TextView
    }

    private fun cargarFragments() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(
                R.id.fragContPrincipal, VideojuegosFragment()
            ) // Agrega el fragmento inicial (VideojuegosFragment) al contenedor
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_principal, menu
        ) // Infla el menú de opciones en la barra de acción
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Maneja la selección de elementos del menú de opciones
        when (item.itemId) {
            R.id.mi_about -> {
                startActivity(Intent(this, AcercaDeActivity::class.java)) // Inicia AcercaDeActivity
            }

            R.id.mi_listaJuegos -> {
                startActivity(
                    Intent(
                        this, ListaJuegosActivity::class.java
                    )
                ) // Inicia ListaJuegosActivity
            }

            R.id.mi_salir -> {
                finishAffinity() // Cierra todas las actividades de la aplicación
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragContPrincipal)

        if (fragment is OnBackPressedListener && fragment.onBackPressed()) {
            // The fragment handled the back press
        } else {
            // Default back press behavior
            super.onBackPressed()
        }
    }
}
