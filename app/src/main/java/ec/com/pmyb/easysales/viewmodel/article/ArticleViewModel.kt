package ec.com.pmyb.easysales.viewmodel.article


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import ec.com.pmyb.easysales.domain.usecase.article.AddArticleUseCase
import ec.com.pmyb.easysales.domain.usecase.article.FetchArtiCodeByIniCodeUseCase
import ec.com.pmyb.easysales.domain.usecase.article.FetchSuppCodeByIniCodeUseCase
import ec.com.pmyb.easysales.domain.usecase.article.GetArticleByCodeOrNameUseCase
import ec.com.pmyb.easysales.domain.usecase.article.GetArticlesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel
@Inject constructor(
    private var getArticlesUseCase: GetArticlesUseCase,
    private var getArticleByCodeOrNameUseCase: GetArticleByCodeOrNameUseCase,
    private var fetchSuppCodeByCodeIniCodeUseCase: FetchSuppCodeByIniCodeUseCase,
    private var fetchArtiCodeByIniCodeUseCase: FetchArtiCodeByIniCodeUseCase,
    private var addArticleUseCase: AddArticleUseCase
) : ViewModel() {

    //  Obtener articulos
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles
    private val _errorArticlesMessage = MutableLiveData<String?>()
    val errorArticlesMessage: LiveData<String?> get() = _errorArticlesMessage
    private val _isLoadingArticles = MutableLiveData<Boolean>()
    val isLoadingArticles: LiveData<Boolean> get() = _isLoadingArticles

    //  Obtener proveedor por codigo
    private val _codeSupplSuggestion = MutableLiveData<String?>()
    val codeSupplSuggestion: LiveData<String?> get() = _codeSupplSuggestion
    private val _errorCodeSupplSuggestionMessage = MutableLiveData<String?>()

    // Obtener ultimo codigo articulo
    private val _codeArticleSuggestion = MutableLiveData<String?>()
    val codeArticleSuggestion: LiveData<String?> get() = _codeArticleSuggestion
    private val _errorCodeArtSuggMessage = MutableLiveData<String?>()

    // Registro articulo
    private val _saveArticleSuccess = MutableLiveData<Boolean>()
    val saveArticleSuccess: LiveData<Boolean> get() = _saveArticleSuccess
    private val _errorSaveArticleMessage = MutableLiveData<String?>()
    val errorSaveArticleMessage: LiveData<String?> get() = _errorSaveArticleMessage
    private val _isSaving = MutableLiveData<Boolean>()
    val isSaving: LiveData<Boolean> get() = _isSaving


    //  Obtener articulos
    private val _fetchedArticles = MutableLiveData<List<Article>?>()
    val fetchedArticles: LiveData<List<Article>?> get() = _fetchedArticles
    private val _errorFetchedArticles = MutableLiveData<String?>()
    val errorFetchedArticles: LiveData<String?> get() = _errorFetchedArticles
    private val _isLoadingFetchedArticles = MutableLiveData<Boolean>()
    val isLoadingFetchedArticles: LiveData<Boolean> get() = _isLoadingFetchedArticles


    init {
        fetchArticles()
    }

    /**
     *
     */
    fun fetchArticles() {
        viewModelScope.launch {
            _isLoadingArticles.value = true
            val result = getArticlesUseCase.invoke()
            if (result.isSuccess) {
                _articles.value = result.getOrDefault(emptyList())
                _errorArticlesMessage.value = null
            }
            if (result.isFailure) {
                _articles.value = emptyList()
                _errorArticlesMessage.value = result.exceptionOrNull()?.message
            }
            _isLoadingArticles.value = false
        }
    }

    /**
     * Obtiene el articulo por codigo de inicializacion
     */
    fun fetchSupplierCodeByCodeInit(iniCode: String) {
        viewModelScope.launch {
            val result = fetchSuppCodeByCodeIniCodeUseCase.invoke(iniCode)
            if (result.isSuccess) {
                _codeSupplSuggestion.value = result.getOrDefault(null)
                _errorCodeSupplSuggestionMessage.value = null
            }
            if (result.isFailure) {
                _codeSupplSuggestion.value = null
                _errorCodeSupplSuggestionMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    /**
     * Obtiene el nuevo codigo de articulo
     */
    fun fetchArticleCodeByCodeInit() {
        viewModelScope.launch {
            val result = fetchArtiCodeByIniCodeUseCase.invoke("FD")
            if (result.isSuccess) {
                _codeArticleSuggestion.value = result.getOrDefault(null)
                _errorCodeArtSuggMessage.value = null
            }
            if (result.isFailure) {
                _codeArticleSuggestion.value = null
                _errorCodeArtSuggMessage.value = result.exceptionOrNull()?.message
            }
        }
    }


    fun saveArticle(article: Article) {
        viewModelScope.launch {
            _isSaving.value = true
            val result = addArticleUseCase.invoke(article)
            if (result.isSuccess) {
                _saveArticleSuccess.value = true
                _errorSaveArticleMessage.value = null
            }
            if (result.isFailure) {
                _saveArticleSuccess.value = false
                _errorSaveArticleMessage.value = result.exceptionOrNull()?.message
            }
            _isSaving.value = false
        }
    }

    fun setUpdateSuccess(value: Boolean){
        _saveArticleSuccess.value = value
    }


    /**
     *
     */
    fun fetchArticlesByCodeOrName(codeOrName: String) {
        viewModelScope.launch {
            _isLoadingFetchedArticles.value = true
            val result = getArticleByCodeOrNameUseCase.invoke(codeOrName)
            if (result.isSuccess) {
                _fetchedArticles.value = result.getOrDefault(emptyList())
                _errorFetchedArticles.value = null
            }
            if (result.isFailure) {
                _fetchedArticles.value = emptyList()
                _errorFetchedArticles.value = result.exceptionOrNull()?.message
            }
            _isLoadingFetchedArticles.value = false
        }
    }

}
