package ec.com.pmyb.easysales.domain.model.vo

data class User(
    var email: String = "",
    var name: String = "",
    var profile: String = "",
    var status: Boolean = true,

//    var profileUser: Profile,
//    var permissionUser: Permission,
)
