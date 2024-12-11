package ec.com.pmyb.easysales.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ec.com.pmyb.easysales.domain.model.ui.UserSession
import ec.com.pmyb.easysales.presentation.utils.AppConstantDataStore.USER_SESSION
import ec.com.pmyb.easysales.presentation.utils.AppConstantDataStore.USER_SESSION_PREFERENCES
import ec.com.pmyb.easysales.presentation.utils.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.sessionDataStore by preferencesDataStore(name = USER_SESSION_PREFERENCES)

class DataStoreManager(private val context: Context) {

    suspend fun clearSessionDataStore(context: Context) {
        context.sessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    //-----------------------------------------------

    private object UserSessionPreferencesKeys {
        val USER_SESSION_PARAM = stringPreferencesKey(USER_SESSION)
    }

    suspend fun saveUserSession(userSession: String) {
        context.sessionDataStore.edit { preferences ->
            preferences[UserSessionPreferencesKeys.USER_SESSION_PARAM] = userSession
        }
    }

    val userSessionFlow: Flow<UserSession?> =
        context.sessionDataStore.data
            .map { preferences ->
                Util.jsonStringToObject<UserSession>(preferences[UserSessionPreferencesKeys.USER_SESSION_PARAM])
            }


}