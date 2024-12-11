package ec.com.pmyb.easysales.domain.model.vo

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuOptionVo(
    var title: Int,
    var icon: ImageVector,
    var route: String,
    var namePermission: String = ""
)
