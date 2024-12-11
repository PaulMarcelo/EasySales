package ec.com.pmyb.easysales.presentation.screens.income

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import ec.com.pmyb.easysales.presentation.utils.NumberVisualTransformation
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import ec.com.pmyb.easysales.presentation.utils.UtilDate
import ec.com.pmyb.easysales.presentation.widgets.ConfirmDialog
import ec.com.pmyb.easysales.presentation.widgets.ProgressDialog
import ec.com.pmyb.easysales.presentation.widgets.RequiredLabel
import ec.com.pmyb.easysales.presentation.widgets.textFieldAppColors
import ec.com.pmyb.easysales.presentation.widgets.textFieldColorsAppDisable
import ec.com.pmyb.easysales.presentation.widgets.TopBarAppWithBack
import ec.com.pmyb.easysales.ui.theme.AppColors
import ec.com.pmyb.easysales.viewmodel.article.ArticleViewModel
import ec.com.pmyb.easysales.viewmodel.supplier.SupplierViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    navController: NavHostController,
    supplierViewModel: SupplierViewModel = hiltViewModel(),
    articleViewModel: ArticleViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    var itemLocalCode by remember { mutableStateOf("") }
    var itemDate by remember { mutableStateOf(UtilDate.getCurrentDate()) }
    var itemSupplierCode by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var itemPvp by remember { mutableStateOf("") }
    var itemPurchasePrice by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("") }
    var itemComment by remember { mutableStateOf("") }

    val listSuppliers by supplierViewModel.supplier.observeAsState(emptyList())
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }

    val codeSupplSuggestion by articleViewModel.codeSupplSuggestion.observeAsState()
    val codeArticleSuggestion by articleViewModel.codeArticleSuggestion.observeAsState()

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var showErrorField by remember { mutableStateOf(false) }

    val colorText = UtilColorApp.getTextColor(isSystemInDarkTheme())
    var showConfirmDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val saveSuccess by articleViewModel.saveArticleSuccess.observeAsState()
    val errorSaveMessage by articleViewModel.errorSaveArticleMessage.observeAsState()
    val isSaving by articleViewModel.isSaving.observeAsState(false)


    LaunchedEffect(Unit) {
        articleViewModel.fetchArticleCodeByCodeInit()
    }
    LaunchedEffect(codeSupplSuggestion) {
        itemSupplierCode = codeSupplSuggestion ?: ""
    }
    LaunchedEffect(codeArticleSuggestion) {
        itemLocalCode = codeArticleSuggestion ?: ""
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBarAppWithBack(
                title = "Ingreso de Artículo",
                navController = navController,
                scrollBehavior = scrollBehavior,
                action = {
                    IconButton(onClick = {
                        val condition =
                            itemSupplierCode.isEmpty() || itemDescription.isEmpty() || itemPvp.isEmpty()
                                    || itemPurchasePrice.isEmpty() || itemQuantity.isEmpty()
                        if (existFieldEmptyRequired(context, condition)) {
                            showErrorField = true
                            return@IconButton
                        }
                        showErrorField = false
                        showConfirmDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Back",
                            tint = AppColors.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(3.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row {
                OutlinedTextField(
                    value = itemDate,
                    onValueChange = { },
                    label = { Text("Fecha") },
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    textStyle = TextStyle(fontSize = 10.sp, color = colorText),
                    enabled = false,
                    colors = textFieldColorsAppDisable()
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = itemLocalCode,//codeArticleSuggestion ?: "FD000001",
                    onValueChange = { itemLocalCode = it },
                    label = { Text("Código local") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 2.dp)
                        .height(55.dp),
                    textStyle = TextStyle(
                        fontSize = 10.sp, color = colorText,
                    ),
                    enabled = false,
                    colors = textFieldColorsAppDisable()
                )
                OutlinedTextField(
                    value = itemSupplierCode,
                    onValueChange = {
                        itemSupplierCode = it
                    },
                    label = {
                        RequiredLabel(text = "Código proveedor")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 2.dp)
                        .height(55.dp),
                    textStyle = TextStyle(fontSize = 10.sp),
                    isError = showErrorField && codeSupplSuggestion?.isEmpty() == true,
                    colors = textFieldAppColors()
                )
            }

            SupplierDropdown(listSuppliers) {
                selectedSupplier = it
                articleViewModel.fetchSupplierCodeByCodeInit(selectedSupplier!!.supplierCode)
            }

            OutlinedTextField(
                value = itemDescription,
                label = {
                    RequiredLabel(text = "Artículo")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                textStyle = TextStyle(fontSize = 11.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                onValueChange = { itemDescription = it },
                colors = textFieldAppColors()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = itemPvp,
                    label = {
                        RequiredLabel(text = "PVP")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 2.dp)
                        .height(55.dp),
                    textStyle = TextStyle(fontSize = 11.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { input ->
                        validateInputNumber(input) {
                            itemPvp = it
                        }
                    },
                    visualTransformation = NumberVisualTransformation(),
                    colors = textFieldAppColors()
                )

                OutlinedTextField(
                    value = itemPurchasePrice,
                    label = {
                        RequiredLabel(text = "Precio compra")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    textStyle = TextStyle(fontSize = 11.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { input ->
                        validateInputNumber(input) {
                            itemPurchasePrice = it
                        }
                    },
                    visualTransformation = NumberVisualTransformation(),
                    colors = textFieldAppColors()
                )
            }
            OutlinedTextField(
                value = itemQuantity,
                label = {
                    RequiredLabel(text = "Cantidad")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                textStyle = TextStyle(fontSize = 11.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { input ->
                    val filteredInput = input.filter { it.isDigit() }.take(3)
                    itemQuantity = filteredInput
                },
                colors = textFieldAppColors()
            )
            OutlinedTextField(
                value = itemCategory,
                label = { Text("Catégoria") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                textStyle = TextStyle(fontSize = 11.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                ),
                onValueChange = { itemCategory = it },
                colors = textFieldAppColors()
            )
            OutlinedTextField(
                value = itemComment,
                label = { Text("Observación") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .height(58.dp),
                textStyle = TextStyle(fontSize = 11.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
                onValueChange = { itemComment = it },
                colors = textFieldAppColors()
            )
        }
    }

    ConfirmDialog(
        message = "Esta seguro de guardar el articulo?",
        showDialog = showConfirmDialog,
        onConfirm = {
            coroutineScope.launch {
                val article = Article()
                article.register_date = itemDate
                article.local_code = itemLocalCode
                article.supplier_code = itemSupplierCode
                article.name = itemDescription
                article.sale_price = itemPvp.toDouble()
                article.supplier_price = itemPurchasePrice.toDouble()
                article.quantity = itemQuantity.toInt()
                article.category = itemCategory
                article.comment = itemComment
                article.status_local = "En Local"
                article.created_at = "28/11/2024"
                article.created_by = "pyaguachi"
                article.created_ip = "192.168.0.1"
                article.status = 1
                articleViewModel.saveArticle(article)
            }
        },
        onDismiss = {
            showConfirmDialog = false
        }
    )

    handleSaveResult(context, saveSuccess, errorSaveMessage,
        success = {
            itemSupplierCode = ""
            itemDescription = ""
            itemPvp = ""
            itemPurchasePrice = ""
            itemQuantity = ""
            itemCategory = ""
            itemComment = ""
            selectedSupplier = null
            articleViewModel.setUpdateSuccess(false)
            articleViewModel.fetchArticleCodeByCodeInit()
        },
        failure = {
            articleViewModel.setUpdateSuccess(false)
        })

    ProgressDialog(
        showDialog = isSaving
    )
}




/**
 *
 */
private fun existFieldEmptyRequired(
    context: Context, condition: Boolean,
): Boolean {
    if (condition) {
        Toast.makeText(
            context,
            "Existen campos requeridos",
            Toast.LENGTH_SHORT
        )
            .show()
        return true
    }
    return false
}


/**
 *
 */
private fun validateInputNumber(input: String, assigValue: (String) -> Unit) {
    val maxInputLength = 6
    val filteredInput = input.filter { it.isDigit() || it == '.' }

    if (filteredInput.count { it == '.' } <= 1) {
        val parts = filteredInput.split(".")
        val integerPart = parts.getOrNull(0)?.take(3) ?: ""
        val decimalPart = parts.getOrNull(1)?.take(2) ?: ""

        val newText = if (filteredInput.endsWith(".") && decimalPart.isEmpty()) {
            "$integerPart."
        } else if (decimalPart.isNotEmpty()) {
            "$integerPart.$decimalPart"
        } else {
            integerPart
        }
        if (newText.length <= maxInputLength) {
            assigValue(newText)
        }
    }
}


/**
 *
 */
@Composable
fun SupplierDropdown(
    suppliers: List<Supplier>, selectedSupplier: (Supplier) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Seleccione") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.label_supplier) + ":",
                modifier = Modifier.padding(end = 5.dp)
            )
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    ) {
                        Text(text = text)
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.dropdown_arrow)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        suppliers.forEach { item ->
                            DropdownMenuItem(text = {
                                Text(item.supplierName)
                            }, onClick = {
                                selectedSupplier(item)
                                text = item.supplierName
                                expanded = false
                            })
                        }
                    }
                }

            }
        }
    }
}

/**
 * Maneja el resultado del registro de un proveedor.
 */
fun handleSaveResult(
    context: Context,
    saveSuccess: Boolean?,
    errorSaveMessage: String?,
    success: () -> Unit,
    failure: () -> Unit
) {
    errorSaveMessage?.let { sms ->
        Toast.makeText(
            context,
            context.getString(R.string.error, sms),
            Toast.LENGTH_SHORT
        ).show()
        failure()
    }
    saveSuccess?.let {
        if (saveSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.register_save),
                Toast.LENGTH_SHORT
            ).show()
            success()
        }
    }
}

