package ec.com.pmyb.easysales.domain.model.vo

data class Profile(
    var name: String = "",
    var status: Boolean = true,
    var permission: List<String> = listOf(),
)
