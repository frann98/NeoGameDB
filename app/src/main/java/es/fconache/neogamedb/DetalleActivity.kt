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
        title = "NeoGameDB" // Establece el título de la actividad en la barra de acción

        setListeners() // Configura los listeners para los botones de la actividad
        recogerYPintarDatos() // Recoge los datos del intent y pinta la interfaz con estos datos
    }

    private fun setListeners() {
        // Listener para el botón de volver atrás
        binding.btnVolver.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }

        // Listener para el botón de agregar a lista de juegos
        binding.btnAgregarALista.setOnClickListener {
            // Obtiene los datos del intent actual
            val datos = intent.extras
            // Inicia AgregarActivity para editar el juego con el nombre del juego actual
            startActivity(
                Intent(this, AgregarActivity::class.java).putExtra(
                    "NOMBREJUEGO", datos?.getString("NOMBRE")
                )
            )
        }
    }

    private fun recogerYPintarDatos() {
        // Obtiene los datos del intent actual
        val datos = intent.extras
        // Extrae la imagen, nombre, fecha de salida y nota del juego del intent
        val imagen = datos?.getString("IMAGEN")
        val nombre = datos?.getString("NOMBRE")
        val salida = datos?.getString("SALIDA")
        val nota = datos?.getInt("NOTA")

        // Utiliza Glide para cargar la imagen desde la URL en el ImageView
        Glide.with(this).load(imagen).into(binding.ivImg)
        // Establece el texto en los TextViews correspondientes con los datos del juego
        binding.tvNombreDetalle.text = "Titulo: " + nombre
        binding.tvFechaSalidaDetalle.text = "Fecha de salida: " + salida
        binding.tvNotaDetalle.text = "Nota en Metacritic: " + nota.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Infla el menú de opciones en la barra de acción
        menuInflater.inflate(R.menu.menu_principal, menu)
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
}
