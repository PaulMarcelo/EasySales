package ec.com.pmyb.easysales.domain.repository

import ec.com.pmyb.easysales.data.datasourse.ArticleFirestoreDBDataSource
import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor
    (
    private val remoteDataSource: ArticleFirestoreDBDataSource
) {

    fun save(article: Article): Result<Boolean> {
        return try {
            remoteDataSource.saveArticle(article)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArticle(): Result<List<Article>> {
        return try {
            val articles = remoteDataSource.getArticles()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     */
    suspend fun getArticlesByCodeOrName(codeOrName: String): Result<List<Article>> {
        return try {
            val articles = remoteDataSource.getArticlesByCodeOrName(codeOrName)
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     */
    suspend fun fetchSupplierCodeByCodeInit(supplierId: String): Result<String> {
        return try {
            val supplier = remoteDataSource.fetchSupplierCodeByCodeInit(supplierId)
            Result.success(supplier)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     */
    suspend fun fetchArticleCodeByCodeInit(iniCode: String): Result<String> {
        return try {
            val supplier = remoteDataSource.fetchArticleCodeByCodeInit(iniCode)
            Result.success(supplier)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}