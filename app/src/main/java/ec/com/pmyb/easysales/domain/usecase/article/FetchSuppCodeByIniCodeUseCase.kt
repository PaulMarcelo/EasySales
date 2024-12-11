package ec.com.pmyb.easysales.domain.usecase.article

import ec.com.pmyb.easysales.domain.repository.ArticleRepository
import javax.inject.Inject

class FetchSuppCodeByIniCodeUseCase @Inject constructor(private val articleRepository: ArticleRepository) {
    suspend operator fun invoke(initCode:String): Result<String> {
        return articleRepository.fetchSupplierCodeByCodeInit(initCode)
    }
}