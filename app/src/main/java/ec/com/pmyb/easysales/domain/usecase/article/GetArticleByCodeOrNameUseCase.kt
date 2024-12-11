package ec.com.pmyb.easysales.domain.usecase.article

import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.domain.repository.ArticleRepository
import javax.inject.Inject

class GetArticleByCodeOrNameUseCase @Inject constructor(private val supplierRepository: ArticleRepository) {
    suspend operator fun invoke(codeOrName: String): Result<List<Article>> {
        return supplierRepository.getArticlesByCodeOrName(codeOrName)
    }
}