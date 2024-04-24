package es.fconache.neogamedb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.fconache.neogamedb.models.VideojuegosModel
import es.fconache.neogamedb.providers.VideojuegosProvider.apiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideojuegosViewModel : ViewModel() {
    private var _listaVideojuegos = MutableLiveData<List<VideojuegosModel>>()
    var listaVideojuegos: LiveData<List<VideojuegosModel>> = _listaVideojuegos

    //--------------------------------------------------------------------------------------------//
    fun obtenerPorBusqueda(q0: String) {
        //Aunque tengo la key en un xml, no paraba de darme problemas para traerla
        //Por eso la puse en una val, y lo hizo funcionar
        val ak = "475edb593b6f47a0b7563fe58fa73d00"
        viewModelScope.launch(Dispatchers.IO) {
            val lista1 = apiClient.getVideojuegos(ak, q0).results
            withContext(Dispatchers.Main) {
                _listaVideojuegos.postValue(lista1)
            }
        }
    }
    //--------------------------------------------------------------------------------------------//

}