package ec.com.pmyb.easysales.data.datasourse

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.presentation.utils.Util
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ArticleFirestoreDBDataSource @Inject constructor() {
    private val NAME_COLLECTION = "data"

    val PARAM_SUPPLIER_CODE = Util.getPropertyName(Article::supplier_code)
    val PARAM_LOCAL_CODE = Util.getPropertyName(Article::local_code)
    val PARAM_NAME = Util.getPropertyName(Article::name)
    private val firestore: FirebaseFirestore = Firebase.firestore

    /**
     *
     */
    fun saveArticle(article: Article) {
        firestore.collection(NAME_COLLECTION)
            .document(article.local_code)
            .set(article)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    /**
     *
     */
    suspend fun getArticles(): List<Article> {
        return firestore.collection(NAME_COLLECTION)
            .orderBy(PARAM_LOCAL_CODE)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(Article::class.java) }
    }

    /**
     * @param code
     * @param name
     */
    suspend fun getArticlesByCodeOrName(codeOrName: String): List<Article> {
        val byCodeQuery = firestore.collection(NAME_COLLECTION)
            .orderBy(PARAM_LOCAL_CODE)
            .startAt(codeOrName)
            .endAt(codeOrName + "\uf8ff")
            .get()
            .await()

        val byNameQuery = firestore.collection(NAME_COLLECTION)
            .orderBy(PARAM_NAME)
            .startAt(codeOrName)
            .endAt(codeOrName + "\uf8ff")
            .get()
            .await()

        val articlesByCode = byCodeQuery.documents.mapNotNull { it.toObject(Article::class.java) }
        val articlesByName = byNameQuery.documents.mapNotNull { it.toObject(Article::class.java) }

        return (articlesByCode + articlesByName).distinctBy { it.local_code }
    }



    /**
     *
     */
    suspend fun fetchSupplierCodeByCodeInit(prefix: String): String {
        val querySnapshot = firestore.collection(NAME_COLLECTION)
            .whereGreaterThanOrEqualTo(PARAM_SUPPLIER_CODE, prefix)
            .whereLessThanOrEqualTo(PARAM_SUPPLIER_CODE, "$prefix\uF8FF")
            .get()
            .await()

        val codes = querySnapshot.documents.mapNotNull { document ->
            val article = document.toObject(Article::class.java)
            article?.supplier_code
        }

        val maxCode = getHighestCode(codes, prefix)
        return incrementCode(maxCode, prefix)
    }

    /**
     *
     */
    suspend fun fetchArticleCodeByCodeInit(prefix: String): String {
        val querySnapshot = firestore.collection(NAME_COLLECTION)
            .whereGreaterThanOrEqualTo(PARAM_LOCAL_CODE, prefix)
            .whereLessThanOrEqualTo(PARAM_LOCAL_CODE, "$prefix\uF8FF")
            .get()
            .await()

        val codes = querySnapshot.documents.mapNotNull { document ->
            val article = document.toObject(Article::class.java)
            article?.local_code
        }

        val maxCode = getHighestCode(codes, prefix)
        return incrementCode(maxCode, prefix)
    }

    /**
     *
     */
    private fun getHighestCode(codes: List<String>, prefix: String): String {
        val maxCode = codes.maxByOrNull { code ->
            code.removePrefix(prefix).toIntOrNull() ?: 0
        }
        return maxCode ?: "0"
    }

    /**
     *
     */
    private fun incrementCode(code: String, prefixInput: String): String {
        var prefix = code.takeWhile { it.isLetter() }
        val number = code.dropWhile { it.isLetter() }.toIntOrNull() ?: 0
        if (prefix.isEmpty()) prefix = prefixInput
        val incrementedNumber = number + 1
        return "$prefix${incrementedNumber}"
    }


}