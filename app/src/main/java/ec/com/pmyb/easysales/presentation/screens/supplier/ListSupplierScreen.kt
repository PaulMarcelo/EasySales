package ec.com.pmyb.easysales.presentation.screens.supplier

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ec.com.pmyb.easysales.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import ec.com.pmyb.easysales.presentation.navigation.SalesScreens
import ec.com.pmyb.easysales.viewmodel.supplier.SupplierViewModel
import ec.com.pmyb.easysales.presentation.widgets.ConfirmDialog
import ec.com.pmyb.easysales.presentation.widgets.ProgressDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSupplierScreen(
    navController: NavController,
    supplierViewModel: SupplierViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    //Obtener la lista de proveedores desde el ViewModel
    val listSuppliers by supplierViewModel.supplier.observeAsState(emptyList())
    val errorListSuppliersMessage by supplierViewModel.errorMessage.observeAsState()
    val isLoadingListSuppliers by supplierViewModel.isLoading.observeAsState(false)
    // Estado para manejar el estado de refresco (pull-to-refresh)
    val isRefreshing by supplierViewModel.isLoading.observeAsState(false)
    //Remover proveedor
    val selectedSupplier = remember { mutableStateOf<Supplier?>(null) }
    var openOptionDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val isDeleting by supplierViewModel.isProcessing.observeAsState(false)
    val deleteSuccess by supplierViewModel.deleteSuccess.observeAsState()
    val errorDeleteMessage by supplierViewModel.errorDeleteMessage.observeAsState()
    //Actualizar proveedor
    var showUpdateDialog by remember { mutableStateOf(false) }
    val updateSuccess by supplierViewModel.updateSuccess.observeAsState()
    val errorUpdateMessage by supplierViewModel.errorUpdateMessage.observeAsState()
    // Estados para manejar la busqueda de proveedores
    var querySearch by remember { mutableStateOf("") }
    val filteredItems =
        listSuppliers.filter { it.supplierName.contains(querySearch, ignoreCase = true) }
    var expandedSearch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        supplierViewModel.fetchSuppliers()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(navController, scrollBehavior, expandedSearch, querySearch,
                onValueChange = {
                    querySearch = it
                },
                onExpandedChange = {
                    expandedSearch = it
                    if (!it) {
                        querySearch = ""
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(SalesScreens.SupplierScreen.name)
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoadingListSuppliers) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                handleGetListSupplierResponse(context, errorListSuppliersMessage)
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        supplierViewModel.fetchSuppliers()
                    }
                ) {
                    LazyColumn {
                        items(filteredItems) { supplier ->
                            SupplierItem(supplier) {
                                selectedSupplier.value = supplier
                                openOptionDialog = true
                            }
                        }
                    }
                }
            }
        }
    }

    var textTitle = ""
    selectedSupplier.value?.let {
        textTitle = it.supplierCode + " - " + it.supplierName
    }
    // Cuadro de dialogo con las opciones: Modificar y Eliminar
    if (openOptionDialog) {
        AlertDialogOption(
            textTitle,
            onDismissRequest = { openOptionDialog = false },
            updateAction = {
                showUpdateDialog = true
                openOptionDialog = false
            },
            deleteAction = {
                showConfirmDialog = true
                openOptionDialog = false
            })
    }

    // Cuadro de confirmacion de eliminar
    ConfirmDialog(
        message = stringResource(R.string.sms_delete_save),
        showDialog = showConfirmDialog,
        onConfirm = {
            supplierViewModel.deleteSupplier(selectedSupplier.value!!.supplierCode)
        },
        onDismiss = {
            showConfirmDialog = false
        }
    )

    AlertDialogUpdate(
        title = textTitle,
        showDialog = showUpdateDialog,
        onDismiss = {
            showUpdateDialog = false
        }, onConfirm = {
            supplierViewModel.updateSupplier(selectedSupplier.value!!.supplierCode, it.trim())
            showUpdateDialog = false
        })

    // Manejo de dialogo de progreso
    ProgressDialog(
        showDialog = isDeleting,
    )

    // Manejo de respuesta de eliminar proveedor
    handleDeleteSupplierResponse(deleteSuccess, errorDeleteMessage,
        success = {
            Toast.makeText(
                context,
                context.getString(R.string.register_delete),
                Toast.LENGTH_SHORT
            ).show()
            supplierViewModel.setRemoveSuccess(false)
            supplierViewModel.fetchSuppliers()
        }, failure = { sms ->
            Toast.makeText(
                context,
                context.getString(R.string.error, sms),
                Toast.LENGTH_SHORT
            ).show()
        })

    // Manejo de respuesta de actualizar proveedor
    handleUpdateSupplierResponse(updateSuccess, errorUpdateMessage,
        success = {
            Toast.makeText(
                context,
                context.getString(R.string.register_update),
                Toast.LENGTH_SHORT
            ).show()
            supplierViewModel.setUpdateSuccess(false)
            supplierViewModel.fetchSuppliers()
        }, failure = { sms ->
            Toast.makeText(
                context,
                context.getString(R.string.error, sms),
                Toast.LENGTH_SHORT
            ).show()
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    expanded: Boolean? = false,
    query: String? = "",
    onValueChange: (value: String) -> Unit,
    onExpandedChange: (value: Boolean) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (expanded!!) {
                SearchBar(
                    inputField = {
                        TextField(
                            value = query!!,
                            onValueChange = {
                                onValueChange(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.search_elipse)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = stringResource(R.string.search)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    onExpandedChange(false)
                                    keyboardController?.hide()
                                }) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        contentDescription = stringResource(R.string.clear_search)
                                    )
                                }
                            }
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = {
                        onExpandedChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        dividerColor = MaterialTheme.colorScheme.primary,
                    ),
                    content = {}
                )
            } else {
                Text(
                    stringResource(R.string.supplier_list),
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (!expanded!!) {
                IconButton(onClick = {
                    keyboardController?.hide()
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (!expanded!!) {
                IconButton(onClick = {
                    onExpandedChange(true)
                    keyboardController?.show()
                }) {
                    Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search))
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun SupplierItem(
    supplier: Supplier,
    onLongPress: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress()
                    })
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    stringResource(
                        R.string.code_label,
                        supplier.supplierCode
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    stringResource(
                        R.string.name_label,
                        supplier.supplierName
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun AlertDialogOption(
    textTitle: String,
    onDismissRequest: () -> Unit,
    updateAction: () -> Unit,
    deleteAction: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            TitleDialog(title = textTitle)
        },
        text = {
            Column {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
                    onClick = {
                        updateAction()
                    }
                ) {
                    Text(stringResource(R.string.update))
                }
                Spacer(modifier = Modifier.padding(4.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
                    onClick = {
                        deleteAction()
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        },
        confirmButton = {
        }
    )
}


@Composable
fun AlertDialogUpdate(
    title: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                TitleDialog(title = title)
            },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(id = R.string.name)) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(text)
                        text = ""
                    }
                ) {
                    Text(stringResource(R.string.accept))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun TitleDialog(title: String){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * Maneja el resultado obtener proveedores.
 */
fun handleGetListSupplierResponse(
    context: Context,
    errorRemoveMessage: String?
) {
    errorRemoveMessage?.let { sms ->
        Toast.makeText(
            context,
            context.getString(R.string.error, sms),
            Toast.LENGTH_SHORT
        ).show()
    }
}

/**
 * Maneja el resultado de eliminar proveedor.
 */
fun handleDeleteSupplierResponse(
    deleteSuccess: Boolean?,
    errorRemoveMessage: String?,
    success: () -> Unit,
    failure: (String) -> Unit
) {
    deleteSuccess?.let {
        if (it) {
            success()
        } else {
            errorRemoveMessage?.let { sms ->
                failure(sms)
            }
        }
    }
}


/**
 * Maneja el resultado de actualizar proveedor.
 */
fun handleUpdateSupplierResponse(
    updateSuccess: Boolean?,
    errorUpdateMessage: String?,
    success: () -> Unit,
    failure: (String) -> Unit
) {
    updateSuccess?.let {
        if (it) {
            success()
        } else {
            errorUpdateMessage?.let { sms ->
                failure(sms)
            }
        }
    }
}

