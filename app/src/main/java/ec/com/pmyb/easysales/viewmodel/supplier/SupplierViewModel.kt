package ec.com.pmyb.easysales.viewmodel.supplier


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.com.pmyb.easysales.domain.usecase.supplier.AddSupplierUseCase
import ec.com.pmyb.easysales.domain.usecase.supplier.GetSupplierUseCase
import ec.com.pmyb.easysales.domain.usecase.supplier.RemoveSupplierUseCase
import ec.com.pmyb.easysales.domain.usecase.supplier.UpdateSupplierUseCase
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel
@Inject constructor(
    private var addSupplierUseCase: AddSupplierUseCase,
    private var getSuppliersUseCase: GetSupplierUseCase,
    private var removeSuppliersUseCase: RemoveSupplierUseCase,
    private var updateSupplierUseCase: UpdateSupplierUseCase,
) : ViewModel() {

    // Registro proveedor
    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _errorRegisterMessage = MutableLiveData<String?>()
    val errorRegisterMessage: LiveData<String?> get() = _errorRegisterMessage

    private val _isRegistering = MutableLiveData<Boolean>()
    val isRegistering: LiveData<Boolean> get() = _isRegistering

    //  Obtener proveedores
    private val _suppliers = MutableLiveData<List<Supplier>>()
    val supplier: LiveData<List<Supplier>> get() = _suppliers

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Eliminar proveedor
    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess

    private val _errorDeleteMessage = MutableLiveData<String?>()
    val errorDeleteMessage: LiveData<String?> get() = _errorDeleteMessage

    private val _isProcessing = MutableLiveData<Boolean>()
    val isProcessing: LiveData<Boolean> get() = _isProcessing

    // Actualizar proveedor
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _errorUpdateMessage = MutableLiveData<String?>()
    val errorUpdateMessage: LiveData<String?> get() = _errorUpdateMessage


    init {
        fetchSuppliers()
    }


    fun registerSupplier(supplier: Supplier) {
        viewModelScope.launch {
            _isRegistering.value = true
            val result = addSupplierUseCase.invoke(supplier)
            if (result.isSuccess) {
                _registerSuccess.value = true
                _errorRegisterMessage.value = null
            }
            if (result.isFailure) {
                _registerSuccess.value = false
                _errorRegisterMessage.value = result.exceptionOrNull()?.message
            }
            _isRegistering.value = false
        }
    }

    fun setRegisterSuccess(value: Boolean){
        _registerSuccess.value = value
    }


    fun fetchSuppliers() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getSuppliersUseCase.invoke()
            if (result.isSuccess) {
                _suppliers.value = result.getOrDefault(emptyList())
                _errorMessage.value = null
            }
            if (result.isFailure) {
                _suppliers.value = emptyList()
                _errorMessage.value = result.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }

    fun deleteSupplier(supplierId: String) {
        viewModelScope.launch {
            _isProcessing.value = true
            val result = removeSuppliersUseCase.invoke(supplierId)
            if (result.isSuccess) {
                _deleteSuccess.value = true
                _errorDeleteMessage.value = null
            }
            if (result.isFailure) {
                _deleteSuccess.value = false
                _errorDeleteMessage.value = result.exceptionOrNull()?.message
            }
            _isProcessing.value = false
        }
    }

    fun setRemoveSuccess(value: Boolean){
        _deleteSuccess.value = value
    }

    fun updateSupplier(supplierId: String, newNameSupplier: String) {
        viewModelScope.launch {
            _isProcessing.value = true
            val result = updateSupplierUseCase.invoke(supplierId, newNameSupplier)
            if (result.isSuccess) {
                _updateSuccess.value = true
                _errorDeleteMessage.value = null
            }
            if (result.isFailure) {
                _updateSuccess.value = false
                _errorDeleteMessage.value = result.exceptionOrNull()?.message
            }
            _isProcessing.value = false
        }
    }

    fun setUpdateSuccess(value: Boolean){
        _updateSuccess.value = value
    }


}
