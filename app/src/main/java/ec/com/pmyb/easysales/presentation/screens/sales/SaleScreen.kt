package ec.com.pmyb.easysales.presentation.screens.sales

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import ec.com.pmyb.easysales.presentation.utils.UtilDate
import ec.com.pmyb.easysales.presentation.widgets.ConfirmDialog
import ec.com.pmyb.easysales.presentation.widgets.TopBarAppWithBack
import ec.com.pmyb.easysales.ui.theme.AppColors
import ec.com.pmyb.easysales.viewmodel.article.ArticleViewModel
import kotlinx.coroutines.launch

data class Producto(val nombre: String, val cantidad: Int, val precio: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreen(
    navController: NavController,
    articleViewModel: ArticleViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var searchQuery by remember { mutableStateOf("") }
    val articlesAdded = remember { mutableStateListOf<Article>() }
    val articleDeleteSelected = remember { mutableStateOf<Article?>(null) }

    var total by remember { mutableStateOf(0.0) }

    var showDeleteDialog by remember { mutableStateOf(false) }


    //Obtener la lista de articulos desde el ViewModel
    val listArticles by articleViewModel.fetchedArticles.observeAsState(null)
    val errorListArticlesMessage by articleViewModel.errorFetchedArticles.observeAsState()
    val isLoadingListArticles by articleViewModel.isLoadingFetchedArticles.observeAsState(false)

    fun agregarProducto(article: Article) {
        val itemFound = articlesAdded.find { it.local_code == article.local_code }
        if (itemFound == null) {
            articlesAdded.add(article)
        } else {
            Toast.makeText(context, "El articulo ya se encuentra agregado", Toast.LENGTH_SHORT)
                .show()
        }

    }

    fun eliminarProducto(article: Article) {
        articlesAdded.remove(article)
    }

    LaunchedEffect(listArticles) {
        listArticles?.let {
            if (it.isNotEmpty()) {
                val article = it[0]
                agregarProducto(article)
            } else {
                Toast.makeText(context, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBarAppWithBack(
                title = "Venta",
                subtitle = "Agregados: ${articlesAdded.size}",
                navController = navController,
                scrollBehavior = scrollBehavior,
                action = {
                    Row {
                        IconButton(
                            onClick = {
                            },
                        ) {
                            Icon(
                                Icons.Filled.Save,
                                contentDescription = "",
                                tint = AppColors.White
                            )
                        }
                        IconButton(
                            onClick = {

                            },
                        ) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "",
                                tint = AppColors.White
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.BlueF),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Codigo: ",
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp, end = 5.dp, bottom = 10.dp),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = "FV0001",
                        color = Color.White,
                        modifier = Modifier.padding(end = 20.dp, bottom = 10.dp),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = "Fecha: ",
                        color = Color.White,
                        modifier = Modifier.padding(end = 5.dp, bottom = 10.dp),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = UtilDate.getCurrentDate(),
                        color = Color.White,
                        modifier = Modifier.padding(end = 16.dp, bottom = 10.dp),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Buscar artículo") },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        value = searchQuery,
                        onValueChange = { it: String ->
                            searchQuery = it
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                articleViewModel.fetchArticlesByCodeOrName(searchQuery)
                                searchQuery = ""
                                keyboardController?.hide()
                            }),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Camera,
                                contentDescription = "",
                                tint = AppColors.BlueF,
                                modifier = Modifier.clickable(onClick = {
                                })
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "",
                                tint = AppColors.BlueF,
                                modifier = Modifier.clickable(onClick = {
                                    if (searchQuery.isNotEmpty()) {
                                        articleViewModel.fetchArticlesByCodeOrName(searchQuery)
                                        searchQuery = ""
                                    }
                                })
                            )
                        })
                }


                Spacer(modifier = Modifier.height(8.dp))

                // Lista de productos
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    itemsIndexed(articlesAdded) { index, producto ->
                        ArticleItem(
                            index = index + 1,
                            article = producto,
                            onDelete = { showDeleteDialog = true }
                        )
                    }
                }

                // Total
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.BlueF)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Total: $${"%.2f".format(total)}",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    )

    ConfirmDialog(
        message = "Esta seguro de eliminar el articulo?",
        showDialog = showDeleteDialog,
        onConfirm = {
            eliminarProducto(articleDeleteSelected.value!!)
        },
        onDismiss = {
            showDeleteDialog = false
        }
    )

    ConfirmDialog(
        message = "Esta seguro de guardar la venta ?",
        showDialog = false,
        onConfirm = {

        },
        onDismiss = {

        }
    )

}


@Composable
fun ArticleItem(
    index: Int,
    article: Article, onDelete: () -> Unit
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgrounColorCard = UtilColorApp.backgroundCardItem(isSystemInDarkTheme)
    val colorText = UtilColorApp.getTextColor(isSystemInDarkTheme)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 1.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgrounColorCard)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.padding(start = 2.dp))
                Text(
                    modifier = Modifier
                        .background(color = AppColors.CremaF_1, RoundedCornerShape(10.dp))
                        .padding(start = 4.dp, end = 6.dp, top = 1.dp, bottom = 1.dp),
                    text = "$index",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorText
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = article.local_code,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = article.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorText
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 2.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 36.dp, end = 40.dp),
                    text = "Cantidad",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorText
                )
                Text(
                    text = "Pvp",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorText
                )
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "Total",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorText
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuantitySelector(colorText, onDelete)
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .weight(1f),
                    text = article.quantity.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorText
                )
                Text(
                    modifier = Modifier
                        .padding(start = 26.dp, top = 8.dp)
                        .weight(1f),
                    text = "$10.98",
                    style = MaterialTheme.typography.titleSmall,
                    color = colorText
                )
            }
        }
    }
}

@Composable
fun QuantitySelector(colorText: Color, onDelete: () -> Unit) {
    var quantity by remember { mutableIntStateOf(1) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .width(200.dp)
            .padding(start = 16.dp)
    ) {

        if (quantity > 1) {
            IconButton(
                onClick = { quantity-- },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "-",
                    tint = colorText
                )
            }
        } else {
            IconButton(
                onClick = {
                    onDelete()
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar",
                    tint = colorText
                )
            }
        }



        BasicTextField(
            value = quantity.toString(),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { quantity = it }
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .width(45.dp)
                        .height(36.dp)
                        .border(1.dp, colorText, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )

        IconButton(
            onClick = { quantity++ },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "+",
                tint = colorText
            )
        }
    }
}


