package ec.com.pmyb.easysales.domain.usecase.article

import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.domain.repository.ArticleRepository
import javax.inject.Inject

class AddArticleUseCase
@Inject
constructor(private val articleRepository: ArticleRepository) {
    operator fun invoke(article: Article): Result<Boolean> {
        return articleRepository.save(article)
    }
}