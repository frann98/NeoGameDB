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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.fconache.neogamedb.databinding.ActivityMainBinding
import es.fconache.neogamedb.shared.Preferences

class MainActivity : AppCompatActivity() {

    private lateinit var pref: Preferences

    private var responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("Register_0", it.resultCode.toString())
            Log.d("Register_01", it.data.toString())
            if (it.resultCode == RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {

                    val cuenta = task.getResult(ApiException::class.java)
                    if (cuenta != null) {
                        val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                        FirebaseAuth.getInstance().signInWithCredential(credenciales)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    irActivityPrincipal()
                                } else {
                                    println("ADVERTENCIA AHORA:")
                                    Log.d("Register_1", it.exception.toString())
                                    println(it.exception.toString())
                                }
                            }
                    }

                } catch (e: ApiException) {
                    println("ADVERTENCIA AHORA:")
                    Log.d("Register_3", e.message.toString())
                    println(e.message.toString())

                }

            } else {
                Toast.makeText(this, "El usuario canceló", Toast.LENGTH_SHORT).show()
            }
        }


    lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "NeoGameDB"
        println("App iniciada")

        auth = Firebase.auth

        pref = Preferences(this)
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
        val usuario = auth.currentUser

        if (usuario != null) {
            irActivityPrincipal()
        }
    }

    private fun setListeners() {
        binding.btnRegister.setOnClickListener {

            if (comprobarCampos()) {

                registroBasico()

            }


        }

        binding.btnLogin.setOnClickListener {

            if (comprobarCampos()) {
                loginBasico()
            }

        }

        binding.swModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref.ponerModo(true)
                activarModoOscuro()
            } else {
                pref.ponerModo(false)
                desactivarModoOscuro()
            }
        }

        binding.btnGoogle.setOnClickListener {

            println("BOTON PULSADO")
            loginGoogle()

        }

    }

    private fun loginBasico() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {

                irActivityPrincipal()

            } else {

                Toast.makeText(this, "Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show()


            }

        }
    }

    private fun registroBasico() {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                loginBasico()
            } else {
                Log.d("REGISTRO", it.exception.toString())
            }
        }.addOnFailureListener {

            Log.d("registro", it.message.toString())

        }
    }

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

    private fun loginGoogle() {

        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id)).requestEmail().build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)

        googleClient.signOut()

        //Lanzamos intent
        responseLauncher.launch(googleClient.signInIntent)

    }

    private fun irActivityPrincipal() {
        startActivity(Intent(this, FragmentsActivity::class.java))
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

    //--------------------------------------------------------------------------------------------//

    private fun desactivarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    //--------------------------------------------------------------------------------------------//

    private fun activarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    //--------------------------------------------------------------------------------------------//


}