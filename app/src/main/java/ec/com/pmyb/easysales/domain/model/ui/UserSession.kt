package ec.com.pmyb.easysales.domain.model.ui

import ec.com.pmyb.easysales.domain.model.vo.Permission
import ec.com.pmyb.easysales.domain.model.vo.Profile
import ec.com.pmyb.easysales.domain.model.vo.User

data class UserSession(
    var user: User,
    var profile: Profile,
//    var permission: Permission,
)
