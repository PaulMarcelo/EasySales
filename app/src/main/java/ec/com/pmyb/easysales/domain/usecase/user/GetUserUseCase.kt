package ec.com.pmyb.easysales.domain.usecase.user

import ec.com.pmyb.easysales.domain.model.ui.UserSession
import ec.com.pmyb.easysales.domain.model.vo.User
import ec.com.pmyb.easysales.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String): Result<UserSession?> {
        return userRepository.getUserByEmail(email)
    }
}