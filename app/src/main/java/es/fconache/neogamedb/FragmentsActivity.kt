package es.fconache.neogamedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.commit
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.fconache.neogamedb.databinding.ActivityFragmentsBinding
import es.fconache.neogamedb.fragments.MusicaFragment
import es.fconache.neogamedb.fragments.VideojuegosFragment
import es.fconache.neogamedb.fragments.VideosFragment

class FragmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentsBinding

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "NeoGameDB"
        auth = Firebase.auth

        pintarEmail()
        setListeners()
        cargarFragments()
    }

    private fun setListeners() {

        binding.btnCerrarSesion.setOnClickListener {

            Firebase.auth.signOut()
            //
            finishAffinity()

        }

        binding.btnMusica.setOnClickListener {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragContPrincipal, MusicaFragment())
            }

        }

        binding.btnVideojuegos.setOnClickListener {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragContPrincipal, VideojuegosFragment())
            }


        }

        binding.btnVideos.setOnClickListener {


            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragContPrincipal, VideosFragment())
            }

        }

    }

    private fun pintarEmail() {
        binding.tvEmail.text = auth.currentUser?.email.toString()
    }

    private fun cargarFragments() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragContPrincipal, VideojuegosFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_about -> {
                startActivity(Intent(this, AcercaDeActivity::class.java))
            }

            R.id.mi_listaJuegos -> {
                startActivity(Intent(this, ListaJuegosActivity::class.java))
            }

            R.id.mi_salir -> {
                finishAffinity()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}