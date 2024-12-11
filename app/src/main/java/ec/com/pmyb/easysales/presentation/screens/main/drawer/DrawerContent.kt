package ec.com.pmyb.easysales.presentation.screens.main.drawer

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.data.local.datastore.DataStoreManager
import ec.com.pmyb.easysales.domain.model.ui.UserSession
import ec.com.pmyb.easysales.domain.model.vo.MenuOptionVo
import ec.com.pmyb.easysales.presentation.navigation.SalesScreens
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import ec.com.pmyb.easysales.presentation.widgets.ConfirmDialog
import ec.com.pmyb.easysales.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    context: Context,
    listOption: List<MenuOptionVo>,
    userSession:UserSession?,
    navController: NavController,
    isItemEnabled: Boolean,
    onClose: () -> Unit
) {
    val dataStoreManager = DataStoreManager(context)
    val coroutineScope = rememberCoroutineScope()
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgrounColorCard = UtilColorApp.backgroundCardItem(isSystemInDarkTheme)
    var showConfirmDialog by remember { mutableStateOf(false) }
    val colorText = UtilColorApp.getTextColor(isSystemInDarkTheme)

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(),
        drawerContainerColor = AppColors.BlueF,
    ) {
        Column(
            Modifier
                .background(color = backgrounColorCard)
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 200.dp)
        ) {
            Column {
                Row {
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 16.dp, start = 16.dp, end = 0.dp, bottom = 5.dp),
                        shape = CircleShape,
                        color = AppColors.White,
                    ) {
                        Image(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(2.dp, AppColors.White, CircleShape)
                        )
                    }
                    Text(
                        text = context.getString(R.string.fudisa_distribuidora_title),
                        modifier = Modifier.padding(top = 50.dp, start = 16.dp),
                        color = colorText,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
                Text(
                    text = (userSession?.user?.name ?: "").plus(" - ")
                        .plus(userSession?.profile?.name ?: ""),
                    color = colorText,
                    modifier = Modifier.padding(start = 16.dp),
                    style = TextStyle(fontSize = 12.sp)
                )
            }
            Text(
                "Menú",
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 5.dp),
                color = colorText
            )
            HorizontalDivider(
                thickness = 2.dp, color = AppColors.White
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            items(listOption) { opcion ->
                if (userSession?.profile?.permission?.contains(opcion.namePermission) == true) {
                    NavigationDrawerItem(
                        icon = {
                            Icon(opcion.icon, contentDescription = null, tint = AppColors.White)
                        },
                        label = {
                            Text(stringResource(id = opcion.title), color = AppColors.White)
                        },
                        selected = false,
                        onClick = {
                            if (!isItemEnabled) {
                                if (opcion.route == "") {
//                            Toast
//                                .makeText(context, opcion.title, Toast.LENGTH_SHORT)
//                                .show()
                                } else {
                                    navController.navigate(opcion.route)
                                }
                                onClose()
                            }
                        },

                    )
                }

            }
            item {
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = AppColors.White
                        )
                    },
                    label = {
                        Text(stringResource(id = R.string.label_logout), color = AppColors.White)
                    },
                    selected = false,
                    onClick = {
                        if (!isItemEnabled) {
                            onClose()
                            showConfirmDialog = true
                        }
                    },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

    ConfirmDialog(
        message = stringResource(R.string.sms_logout_confirmation),
        showDialog = showConfirmDialog,
        onConfirm = {
            coroutineScope.launch {
                dataStoreManager.clearSessionDataStore(context)
                navController.navigate(SalesScreens.LoginScreen.name) {
                    popUpTo(SalesScreens.LoginScreen.name) { inclusive = true }
                    launchSingleTop = true
                }
            }
        },
        onDismiss = {
            showConfirmDialog = false
        }
    )

}

