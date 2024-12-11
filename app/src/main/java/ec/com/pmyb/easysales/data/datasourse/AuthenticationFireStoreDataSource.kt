package ec.com.pmyb.easysales.data.datasourse

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationFireStoreDataSource @Inject constructor() {

    suspend fun signInWithEmailPassword(email: String, password: String): Boolean {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            false
        } catch (e: FirebaseAuthUserCollisionException) {
            false
        } catch (e: Exception) {
            false
        }
    }

}