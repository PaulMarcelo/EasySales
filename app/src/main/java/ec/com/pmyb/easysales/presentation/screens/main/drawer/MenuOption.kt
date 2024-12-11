package ec.com.pmyb.easysales.presentation.screens.main.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.domain.model.vo.MenuOptionVo
import ec.com.pmyb.easysales.presentation.navigation.SalesScreens
import ec.com.pmyb.easysales.presentation.utils.AppPermission.INCOME
import ec.com.pmyb.easysales.presentation.utils.AppPermission.SALES
import ec.com.pmyb.easysales.presentation.utils.AppPermission.SUPPLIER

object MenuOption {
    fun getMenuOptions(): List<MenuOptionVo> {
        return listOf(
            MenuOptionVo(
                title = R.string.label_sales, icon = Icons.Filled.ShoppingCart, "",
                SALES,
            ),
            MenuOptionVo(
                title = R.string.label_inventory, icon = Icons.Filled.Home, "",
                "nd",
            ),
            MenuOptionVo(
                title = R.string.label_income,
                icon = Icons.Filled.AddCircle,
                SalesScreens.IncomeScreen.name,
                INCOME,
            ),
            MenuOptionVo(
                title = R.string.label_supplier,
                icon = Icons.Filled.Badge,
                SalesScreens.ListSupplierScreen.name,
                SUPPLIER,
            ),
            MenuOptionVo(
                title = R.string.label_other, icon = Icons.Filled.Star, "nd"
            ),
        )
    }
}