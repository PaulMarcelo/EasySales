package ec.com.pmyb.easysales.presentation.screens.supplier

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ec.com.pmyb.easysales.R
import androidx.hilt.navigation.compose.hiltViewModel
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import ec.com.pmyb.easysales.viewmodel.supplier.SupplierViewModel
import ec.com.pmyb.easysales.presentation.utils.AppConstant.EMPTY_STRING
import ec.com.pmyb.easysales.presentation.widgets.ConfirmDialog
import ec.com.pmyb.easysales.presentation.widgets.ProgressDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierScreen(
    navController: NavController,
    supplierViewModel: SupplierViewModel = hiltViewModel()
) {

    var showConfirmDialog by remember { mutableStateOf(false) }

    var supplierCode by remember { mutableStateOf(EMPTY_STRING) }
    var supplierName by remember { mutableStateOf(EMPTY_STRING) }
    val scrollState = rememberScrollState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val supplierCodeFocusRequester = remember { FocusRequester() }


    val registerSuccess by supplierViewModel.registerSuccess.observeAsState()
    val errorRegisterMessage by supplierViewModel.errorRegisterMessage.observeAsState()
    val isRegistering by supplierViewModel.isRegistering.observeAsState(false)


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SupplierTopAppBar(navController, scrollBehavior) {
                if (supplierCode.isEmpty() || supplierName.isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.complete_field), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showConfirmDialog = true
                }
            }
        }
    ) { innerPadding ->
        handleRegisterResult(context, registerSuccess, errorRegisterMessage,
            success = {
                supplierCode = EMPTY_STRING
                supplierName = EMPTY_STRING
                showConfirmDialog = false
                supplierCodeFocusRequester.requestFocus()
                supplierViewModel.setRegisterSuccess(false)
            },
            failure = {
                supplierCodeFocusRequester.requestFocus()
            })
        SupplierForm(
            innerPadding = innerPadding,
            scrollState = scrollState,
            supplierCode = supplierCode,
            onSupplierCodeChange = { supplierCode = it },
            supplierName = supplierName,
            onSupplierNameChange = { supplierName = it },
            supplierCodeFocusRequester
        )
    }

    ConfirmDialog(
        message = stringResource(R.string.sms_confirmation_save),
        showDialog = showConfirmDialog,
        onConfirm = {
            val supplier = Supplier()
            supplier.supplierCode = supplierCode.trim()
            supplier.supplierName = supplierName.trim()
            supplierViewModel.registerSupplier(supplier)

        },
        onDismiss = {
            showConfirmDialog = false
        }
    )

    ProgressDialog(
        showDialog = isRegistering,
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierTopAppBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    onShowDialog: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.label_supplier),
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onShowDialog()
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = stringResource(R.string.save)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


@Composable
fun SupplierForm(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    supplierCode: String,
    onSupplierCodeChange: (String) -> Unit,
    supplierName: String,
    onSupplierNameChange: (String) -> Unit,
    supplierCodeFocusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        OutlinedTextField(
            value = supplierCode,
            label = { Text(stringResource(R.string.code)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .focusRequester(supplierCodeFocusRequester),
            textStyle = TextStyle(fontSize = 11.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            maxLines = 1,
            onValueChange = {
                if (it.length <= 4) {
                    onSupplierCodeChange(it.uppercase())
                }
            }
        )

        OutlinedTextField(
            value = supplierName,
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .height(58.dp),
            textStyle = TextStyle(fontSize = 11.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            onValueChange = onSupplierNameChange,
        )
    }
}


/**
 * Maneja el resultado del registro de un proveedor.
 */
fun handleRegisterResult(
    context: Context,
    registerSuccess: Boolean?,
    errorRegisterMessage: String?,
    success: () -> Unit,
    failure: () -> Unit
) {
    errorRegisterMessage?.let { sms ->
        Toast.makeText(
            context,
            context.getString(R.string.error, sms),
            Toast.LENGTH_SHORT
        ).show()
        failure()
    }
    registerSuccess?.let {
        if (registerSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.register_save),
                Toast.LENGTH_SHORT
            ).show()
            success()
        }
    }
}


