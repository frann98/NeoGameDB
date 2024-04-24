package es.fconache.neogamedb

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.fconache.neogamedb.databinding.ActivityAcercaDeBinding

class AcercaDeActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    val LOCATION_PERMISSION = 1000

    private lateinit var binding: ActivityAcercaDeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcercaDeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = SupportMapFragment()
        fragment.getMapAsync(this)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fcv_mapa, fragment)
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            }
        } else {
            Toast.makeText(this, "El usuario denegó los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMyLocationClickListener(this)
        map.setOnMyLocationButtonClickListener(this)

        ponerLocation()
        mostrarAnimacion(LatLng(36.85028226361124, -2.4650320659022507))

        ponerMarcador()
    }

    private fun ponerMarcador() {
        //
        val coordenadas = LatLng(36.85028226361124, -2.4650320659022507)
        val marcador = MarkerOptions().position(coordenadas).title("Marcador Jaen")
        map.addMarker(marcador)
    }

    private fun mostrarAnimacion(latLng: LatLng) {
        //
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f), 3000, null)
    }

    private fun ponerLocation() {
        if (!::map.isInitialized) return //si no esta inicializado salimos
        //Importar manifest de android
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            map.isMyLocationEnabled = true

        } else {
            pedirPermisos()
        }
    }

    private fun pedirPermisos() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            //han denegado los permisos
            mostrarExplicacion()
        } else {
            //Pedimos los permisos
            escogerPermisos()
        }
    }

    private fun escogerPermisos() {
        //
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION
        )
    }

    private fun mostrarExplicacion() {
        //
        AlertDialog.Builder(this).setTitle("Permisos Localización")
            .setMessage("La app necesita permiso de localización para funcionar correctamente")
            .setPositiveButton("Abrir Ajustes") { view, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                view.dismiss()
            }.setNegativeButton("CANCELAR", null).setCancelable(false).create().show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Has pulsado el boton", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(
            this, "Coordenadas: Lat: ${p0.latitude}, Long: ${p0.longitude}", Toast.LENGTH_LONG
        ).show()
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