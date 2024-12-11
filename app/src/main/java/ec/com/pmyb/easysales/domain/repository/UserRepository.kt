package ec.com.pmyb.easysales.domain.repository

import ec.com.pmyb.easysales.data.datasourse.UsersFireStoreDBDataSource
import ec.com.pmyb.easysales.domain.model.ui.UserSession
import ec.com.pmyb.easysales.domain.model.vo.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor
    (
    private val remoteDataSource: UsersFireStoreDBDataSource,
) {
    suspend fun getUserByEmail(email: String): Result<UserSession?> {
        return try {
            val suppliers = remoteDataSource.getUserByEmail(email)
            Result.success(suppliers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}