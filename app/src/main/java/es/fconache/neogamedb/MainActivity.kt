package es.fconache.neogamedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.fconache.neogamedb.databinding.ActivityMainBinding
import es.fconache.neogamedb.shared.Preferences

class MainActivity : AppCompatActivity() {

    private lateinit var pref: Preferences

    // Launcher para obtener el resultado de la actividad de Google SignIn
    private var responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    // Obtiene la cuenta de Google
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        // Obtiene las credenciales de autenticación de Google y las utiliza para iniciar sesión en Firebase
                        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                        FirebaseAuth.getInstance().signInWithCredential(credentials)
                            .addOnCompleteListener { authResult ->
                                if (authResult.isSuccessful) {
                                    irActivityPrincipal()
                                } else {
                                    Log.e(
                                        "GoogleSignIn",
                                        "Google sign in failed: ${authResult.exception}"
                                    )
                                    Toast.makeText(
                                        this,
                                        "Error al iniciar sesión con Google",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Google sign in failed: ${e.message}")
                    Toast.makeText(
                        this, "Error al iniciar sesión con Google: ${e.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "El usuario canceló", Toast.LENGTH_SHORT).show()
            }
        }

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "NeoGameDB"

        auth = Firebase.auth
        pref = Preferences(this)

        // Configura el modo oscuro según la preferencia almacenada
        if (pref.leerModo()) {
            activarModoOscuro()
            binding.swModoOscuro.isChecked = true
        } else {
            desactivarModoOscuro()
            binding.swModoOscuro.isChecked = false
        }

        setListeners()
    }

    override fun onStart() {
        super.onStart()
        // Verifica si el usuario ya está autenticado al iniciar la actividad
        val currentUser = auth.currentUser
        if (currentUser != null) {
            irActivityPrincipal()
        }
    }

    private fun setListeners() {
        // Listener para el botón de registro
        binding.btnRegister.setOnClickListener {
            if (comprobarCampos()) {
                registroBasico()
            }
        }

        // Listener para el botón de inicio de sesión
        binding.btnLogin.setOnClickListener {
            if (comprobarCampos()) {
                loginBasico()
            }
        }

        // Listener para el switch de modo oscuro
        binding.swModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref.ponerModo(true)
                activarModoOscuro()
            } else {
                pref.ponerModo(false)
                desactivarModoOscuro()
            }
        }

        // Listener para el botón de inicio de sesión con Google
        binding.btnGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    // Método para iniciar sesión con correo electrónico y contraseña
    private fun loginBasico() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    irActivityPrincipal()
                } else {
                    Toast.makeText(this, "Email o Contraseña incorrectos", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    // Método para registrar un nuevo usuario con correo electrónico y contraseña
    private fun registroBasico() {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loginBasico()
                } else {
                    Toast.makeText(
                        this,
                        "Error al registrar usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // Método para validar los campos de email y contraseña
    private fun comprobarCampos(): Boolean {
        email = binding.etEmail.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Introduce un Email válido"
            return false
        }

        password = binding.etPassword.text.toString().trim()
        if (password.length < 6) {
            binding.etPassword.error = "La contraseña debe tener mínimo 6 caracteres"
            return false
        }
        return true
    }

    // Método para iniciar sesión con Google
    private fun loginGoogle() {

        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id)).requestEmail().build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)

        googleClient.signOut()

        //Lanzamos intent
        responseLauncher.launch(googleClient.signInIntent)

    }

    // Método para navegar a la actividad principal
    private fun irActivityPrincipal() {
        startActivity(Intent(this, FragmentsActivity::class.java))
        finish()
    }

    // Métodos para crear el menú de opciones en la barra de acción
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

    // Métodos para activar y desactivar el modo oscuro
    private fun activarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun desactivarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
