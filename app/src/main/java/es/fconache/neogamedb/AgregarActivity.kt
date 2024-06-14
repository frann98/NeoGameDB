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

    // Variables para almacenar los datos del videojuego
    private var id = -1
    private var nombre = ""
    private var estado = ""
    private var notaPers = 0

    // Lista de estados para el spinner
    private val spinerItems = listOf("Seleccionar estado", "Pasado", "Jugando", "Deseado")

    // Bandera para indicar si se está actualizando un registro existente
    private var update = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recuperarVideojuego() // Recupera el videojuego si se está editando

        // Configuración inicial de la interfaz según sea un nuevo videojuego o edición
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

        setListeners() // Configura los listeners de los componentes de la actividad
    }

    // Método para recuperar los datos del videojuego si se está editando
    private fun recuperarVideojuego() {
        val vj = intent.getSerializableExtra("VIDEOJUEGO")
        if (vj != null) {
            update = true
            val videojuego = vj as VideojuegosSerializable
            id = videojuego.id
            nombre = videojuego.nombre
            estado = videojuego.estado
            notaPers = videojuego.notaPersonal
            pintarDatos() // Pinta los datos del videojuego en la interfaz gráfica
        } else {
            update = false
        }
    }

    // Método para pintar los datos del videojuego en la interfaz gráfica
    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etEstado.setText(estado)
        binding.sbNotaPersonal.progress = notaPers
        binding.etNombre.requestFocus()
    }

    // Método para configurar los listeners de los componentes de la actividad
    private fun setListeners() {
        // Configuración del Spinner de estados
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
                // No se hace nada cuando no se selecciona nada en el spinner
            }
        }

        // Configuración del SeekBar de nota personal
        binding.sbNotaPersonal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                notaPers = progress
                binding.tvIntnota.text = binding.sbNotaPersonal.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No se realiza ninguna acción al inicio del seguimiento del SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No se realiza ninguna acción al finalizar el seguimiento del SeekBar
            }
        })

        // Configuración de los botones de la actividad
        binding.btnCancelar.setOnClickListener {
            finish() // Cierra la actividad actual
        }

        binding.btn2Reset.setOnClickListener {
            limpiar() // Limpia los campos del formulario
        }

        binding.btn2Enviar.setOnClickListener {
            if (binding.spnEstado.selectedItem == spinerItems[0]) {
                binding.etEstado.error = "Debe seleccionar un estado"
            }
            if (procesarFormulario()) {
                guardarRegistro() // Guarda o actualiza el registro del videojuego
            }
        }
    }

    // Método para guardar o actualizar el registro del videojuego
    private fun guardarRegistro() {
        val videojuego = VideojuegosSerializable(id, nombre, estado, notaPers)
        val ac = VideojuegosDBAdmin()

        if (!update) { // Si es un nuevo videojuego
            val resultado = ac.create(videojuego)
            if (resultado != -1L) {
                Toast.makeText(this, "Videojuego Guardado", Toast.LENGTH_LONG).show()
                finish() // Cierra la actividad después de guardar el videojuego
            } else {
                binding.etNombre.error = "ERROR, El nombre ya existe"
                binding.etNombre.requestFocus()
            }
        } else { // Si se está editando un videojuego existente
            if (!ac.update(videojuego)) {
                binding.etNombre.error = "ERROR, El nombre ya existe"
                binding.etNombre.requestFocus()
            } else {
                Toast.makeText(this, "Videojuego Editado", Toast.LENGTH_LONG).show()
                finish() // Cierra la actividad después de editar el videojuego
            }
        }
    }

    // Método para limpiar los campos del formulario
    private fun limpiar() {
        val datos = intent.extras
        binding.etNombre.setText(datos?.getString("NOMBREJUEGO"))
        binding.spnEstado.setSelection(0)
        binding.etEstado.setText("")
        binding.sbNotaPersonal.progress = 0
        binding.etNombre.requestFocus()
    }

    // Método para validar y procesar el formulario antes de guardar o actualizar
    private fun procesarFormulario(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        if (nombre.length < 3) {
            binding.etNombre.error = "El nombre debe tener al menos 3 caracteres"
            return false
        }
        estado = binding.etEstado.text.toString().trim()
        if (estado.isEmpty()) {
            binding.etEstado.error = "El estado no puede estar vacío"
            return false
        }
        return true
    }
}
