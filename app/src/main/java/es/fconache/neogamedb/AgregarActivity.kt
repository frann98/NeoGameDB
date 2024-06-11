package es.fconache.neogamedb

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import es.fconache.neogamedb.databases.VideojuegosDBAdmin
import es.fconache.neogamedb.databinding.ActivityAgregarBinding
import es.fconache.neogamedb.models.VideojuegosSerializable

class AgregarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarBinding


    private var id = -1
    private var nombre = ""
    private var estado = ""
    private var notaPers = 0
    private val spinerItems = listOf("Seleccionar estado", "Pasado", "Jugando", "Deseado")
    private var update = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recuperarVideojuego()
        if (update) {
            title = "EDITAR VIDEOJUEGO"
            binding.btn2Enviar.setText("EDITAR")
            binding.tvTitulo.text = "EDITAR VIDEOJUEGO"
        } else {
            title = "NUEVO VIDEOJUEGO"
            binding.btn2Enviar.setText("AGREGAR")
            binding.tvTitulo.text = "AGREGAR VIDEOJUEGO"
            val nombreJuego = intent.getStringExtra("NOMBREJUEGO")
            binding.etNombre.setText(nombreJuego)
        }

        setListeners()

    }

    //--------------------------------------------------------------------------------------------//

    private fun recuperarVideojuego() {
        val vj = intent.getSerializableExtra("VIDEOJUEGO")
        if (vj != null) {
            update = true
            val videojuego = vj as VideojuegosSerializable
            id = videojuego.id
            nombre = videojuego.nombre
            estado = videojuego.estado
            notaPers = videojuego.notaPersonal
            pintarDatos()
        } else {
            update = false
        }
    }

    //--------------------------------------------------------------------------------------------//

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etEstado.setText(estado)
        binding.sbNotaPersonal.progress = notaPers
        binding.etNombre.requestFocus()
    }

    //--------------------------------------------------------------------------------------------//

    private fun setListeners() {

        val spnAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, spinerItems)

        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spnEstado.adapter = spnAdapter

        binding.spnEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                var selectedItem = spinerItems[position]
                if (selectedItem == spinerItems[0]) {
                    binding.etEstado.setText("")
                } else {
                    binding.etEstado.setText(selectedItem)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

        }


        binding.sbNotaPersonal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                notaPers = p1
                binding.tvIntnota.text = binding.sbNotaPersonal.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })



        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btn2Reset.setOnClickListener {
            limpiar()
        }

        binding.btn2Enviar.setOnClickListener {
            if (binding.spnEstado.selectedItem == spinerItems[0]) {
                binding.etEstado.error = "Debe seleccionar un estado"
            }
            if (procesarFormulario()) {
                guardarRegistro()
            }
        }
    }

    //--------------------------------------------------------------------------------------------//

    private fun guardarRegistro() {
        val videojuego = VideojuegosSerializable(id, nombre, estado, notaPers)
        val ac = VideojuegosDBAdmin()
        if (!update) {
            val resultado = ac.create(videojuego)
            if (resultado != -1L) {
                Toast.makeText(this, "Videojuego Guardado", Toast.LENGTH_LONG).show()
                finish()
            } else {
                binding.etNombre.error = "ERROR, El nombre ya existe"
                binding.etNombre.requestFocus()
            }
        } else {
            if (!ac.update(videojuego)) {
                binding.etNombre.error = "ERROR, El nombre ya existe"
                binding.etNombre.requestFocus()
            } else {
                Toast.makeText(this, "Videojuego Editado", Toast.LENGTH_LONG).show()
                finish()
            }

        }
    }

    //--------------------------------------------------------------------------------------------//

    private fun limpiar() {
        val datos = intent.extras
        binding.etNombre.setText(datos?.getString("NOMBREJUEGO"))
        binding.spnEstado.setSelection(0)
        binding.etEstado.setText("")
        binding.sbNotaPersonal.progress = 0
        binding.etNombre.requestFocus()
    }

    //--------------------------------------------------------------------------------------------//

    private fun procesarFormulario(): Boolean {

        nombre = binding.etNombre.text.toString().trim()
        if (nombre.length < 3) {
            binding.etNombre.error = "El nombre debe tener 4 caracteres como poco"
            return false
        }
        estado = binding.etEstado.text.toString().trim()
        if (estado == "") {
            binding.etEstado.error = "El estado no puede ser nulo"
            return false
        }
        return true
    }
}