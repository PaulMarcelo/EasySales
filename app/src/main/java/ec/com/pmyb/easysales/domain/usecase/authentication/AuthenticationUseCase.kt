package ec.com.pmyb.easysales.domain.usecase.authentication

import ec.com.pmyb.easysales.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationUseCase
@Inject
constructor(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return authenticationRepository.signInWithEmailPassword(email, password)
    }
}