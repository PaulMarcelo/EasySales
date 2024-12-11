package ec.com.pmyb.easysales.viewmodel.authentication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.com.pmyb.easysales.domain.usecase.authentication.AuthenticationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticactionViewModel
@Inject constructor(
    private var authenticationUseCase: AuthenticationUseCase,
) : ViewModel() {

    private val _authenticationSuccess = MutableLiveData<Boolean>()
    val authenticationSuccess: LiveData<Boolean> get() = _authenticationSuccess

    private val _errorAuthenticationMessage = MutableLiveData<String?>()
    val errorAuthenticationMessage: LiveData<String?> get() = _errorAuthenticationMessage
//
//    private val _isAuthenticating = MutableLiveData<Boolean>()
//    val isAuthenticating: LiveData<Boolean> get() = _isAuthenticating


    fun signInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
//            _isAuthenticating.value = true
            val result = authenticationUseCase.invoke(email, password)
            if (result.isSuccess) {
                _authenticationSuccess.value = true
                _errorAuthenticationMessage.value = null
            }
            if (result.isFailure) {
                _authenticationSuccess.value = false
                _errorAuthenticationMessage.value = result.exceptionOrNull()?.message
            }
//            _isAuthenticating.value = false
        }
    }

//    fun setIsAuthenticating(value: Boolean) {
//        _isAuthenticating.value = value
//    }

}
