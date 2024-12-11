package ec.com.pmyb.easysales.domain.repository

import ec.com.pmyb.easysales.data.datasourse.AuthenticationFireStoreDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepository @Inject constructor
    (
    private val authenticationDataSource: AuthenticationFireStoreDataSource,
) {

    suspend fun signInWithEmailPassword(email: String, password: String): Result<Boolean> {
        return try {
            authenticationDataSource.signInWithEmailPassword(email, password)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}