package es.fconache.neogamedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import es.fconache.neogamedb.databinding.ActivityDetalleBinding

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "NeoGameDB"

        setListeners()
        recogerYPintarDatos()
    }

    private fun setListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnAgregarALista.setOnClickListener {
            startActivity(Intent(this, AgregarActivity::class.java))
        }
    }

    private fun recogerYPintarDatos() {
        val datos = intent.extras
        val imagen = datos?.getString("IMAGEN")
        val nombre = datos?.getString("NOMBRE")
        val salida = datos?.getString("SALIDA")
        val nota = datos?.getInt("NOTA")

        Glide.with(this).load(imagen).into(binding.ivImg)
        binding.tvNombreDetalle.text = "Titulo: " + nombre
        binding.tvFechaSalidaDetalle.text = "Fecha de salida: " + salida
        binding.tvNotaDetalle.text = "Nota en Metacritic: " + nota.toString()
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

