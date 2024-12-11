package ec.com.pmyb.easysales.viewmodel.user


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.com.pmyb.easysales.data.local.datastore.DataStoreManager
import ec.com.pmyb.easysales.domain.model.ui.UserSession
import ec.com.pmyb.easysales.domain.usecase.user.GetUserUseCase
import ec.com.pmyb.easysales.presentation.utils.Util
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject constructor(
    private var getUserUseCase: GetUserUseCase
) : ViewModel() {


    private val _userSession = MutableLiveData<UserSession?>()

    fun getUserByEmail(email: String, context: Context) {
        val dataStoreManager = DataStoreManager(context)
        viewModelScope.launch {
            val result = getUserUseCase.invoke(email)
            if (result.isSuccess) {
                _userSession.value = result.getOrNull()
                _userSession.value?.let {
                    val jsonObj = Util.objectToJsonString(it)
                    dataStoreManager.saveUserSession(jsonObj)
                }
            }
            if (result.isFailure) {
                _userSession.value = null
            }
        }
    }

}
