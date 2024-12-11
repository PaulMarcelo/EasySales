package ec.com.pmyb.easysales.presentation.screens.main

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.data.local.datastore.DataStoreManager
import ec.com.pmyb.easysales.domain.model.vo.Article
import ec.com.pmyb.easysales.presentation.navigation.SalesScreens
import ec.com.pmyb.easysales.presentation.screens.main.drawer.DrawerContent
import ec.com.pmyb.easysales.presentation.screens.main.drawer.MenuOption
import ec.com.pmyb.easysales.presentation.utils.AppPermission.CONSULTS
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import ec.com.pmyb.easysales.ui.theme.AppColors
import ec.com.pmyb.easysales.viewmodel.article.ArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    articleViewModel: ArticleViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    //Obtener la lista de articulos desde el ViewModel
    val listArticles by articleViewModel.articles.observeAsState(emptyList())
    val errorListArticlesMessage by articleViewModel.errorArticlesMessage.observeAsState()
    val isLoadingListArticles by articleViewModel.isLoadingArticles.observeAsState(false)

    // Estado para manejar el estado de refresco (pull-to-refresh)
    val isRefreshing by articleViewModel.isLoadingArticles.observeAsState(false)

    var querySearch by remember { mutableStateOf("") }
    val filteredItems =
        listArticles.filter {
            it.local_code.contains(querySearch, ignoreCase = true) ||
                    it.name.contains(querySearch, ignoreCase = true)
                    || it.sale_price.toString().contains(querySearch, ignoreCase = true)
        }
    var expandedSearch by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val dataStoreManager = DataStoreManager(context)
    val userSession = dataStoreManager.userSessionFlow.collectAsState(initial = null)
    var isFabExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var isFabVisible by remember { mutableStateOf(true) }
    var lastScrollOffset by remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        articleViewModel.fetchArticles()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (_, offset) ->
                if (offset > lastScrollOffset) {
                    isFabVisible = false
                } else if (offset < lastScrollOffset) {
                    isFabVisible = true
                }
                lastScrollOffset = offset
            }
    }

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                context = context,
                listOption = MenuOption.getMenuOptions(),
                navController = navController,
                userSession = userSession.value,
                isItemEnabled = isFabExpanded,
                onClose = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        val permissionConsult = (userSession.value?.profile?.permission?.contains(CONSULTS) == true)
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(coroutineScope, drawerState,
                    scrollBehavior, expandedSearch, querySearch, filteredItems,
                    focusRequester,
                    permissionConsult,
                    isFabExpanded,
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
            }, content = { padding ->
                if (!permissionConsult) {
                    return@Scaffold
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    if (isLoadingListArticles) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            color = AppColors.BlueF,
                            trackColor = AppColors.CremaF
                        )
                    } else {
                        handleGetListArticlesResponse(context, errorListArticlesMessage)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (expandedSearch) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = AppColors.BlueF,
                                            shape = RectangleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp, vertical = 4.dp),
                                        text = "Total artículos: ${filteredItems.size}",
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = AppColors.White
                                    )
                                }
                            }
                            SwipeRefresh(
                                state = rememberSwipeRefreshState(isRefreshing),
                                onRefresh = {
                                    expandedSearch = false
                                    querySearch = ""
                                    articleViewModel.fetchArticles()
                                }
                            ) {
                                LazyColumn(
                                    state = listState,
                                    userScrollEnabled = !isFabExpanded
                                ) {
                                    items(filteredItems) { supplier ->
                                        ArticleItem(supplier) {
//                                        selectedSupplier.value = supplier
//                                        openOptionDialog = true
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFabVisible,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    FloatingActionButtonMain(isFabExpanded, navController) {
                        isFabExpanded = !isFabExpanded
                    }
                }

            }
        )
    }

    BackHandler {
        navController.popBackStack()
        (context as? Activity)?.finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior,
    expanded: Boolean? = false,
    query: String? = "",
    filteredItems: List<Article>,
    focusRequester: FocusRequester,
    permissionConsults: Boolean,
    isFabExpanded: Boolean,
    onValueChange: (value: String) -> Unit,
    onExpandedChange: (value: Boolean) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(expanded) {
        if (expanded!!) {
            focusRequester.requestFocus()
        }
    }
    TopAppBar(
        title = {
            if (expanded!!) {
                SearchBar(
                    inputField = {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .focusRequester(focusRequester),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                            ),
                            value = query!!,
                            onValueChange = {
                                onValueChange(it)
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_elipse),
                                    color = AppColors.White
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = stringResource(R.string.search),
                                    tint = AppColors.White
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    onExpandedChange(false)
                                    keyboardController?.hide()
                                }) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        contentDescription = stringResource(R.string.clear_search),
                                        tint = AppColors.White
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = AppColors.White,
                                unfocusedTextColor = AppColors.White,
                                focusedContainerColor = AppColors.BlueF,
                                unfocusedContainerColor = AppColors.BlueF,
                                cursorColor = AppColors.White
                            )
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = {
                        onExpandedChange(it)
                    },
                    content = {},
                    colors = SearchBarDefaults.colors(
                        containerColor = AppColors.BlueF,
                        dividerColor = Color.Transparent
                    )
                )
            } else {
                Column {
                    Text(
                        stringResource(R.string.fudisa_distribuidora_title),
                        overflow = TextOverflow.Ellipsis,
                        color = AppColors.White
                    )
                    if (permissionConsults) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Total artículos: ${filteredItems.size}",
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.White
                        )
                    }

                }
            }
        },
        navigationIcon = {
            if (!expanded!!) {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    enabled = !isFabExpanded
                ) {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = "Open Drawer",
                        tint = AppColors.White
                    )
                }
            }
        }, actions = {
            if (!expanded!!) {
                IconButton(
                    onClick = {
                        onExpandedChange(true)
                        keyboardController?.show()
                    },
                    enabled = !isFabExpanded
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search),
                        tint = AppColors.White
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.BlueF,
            scrolledContainerColor = AppColors.BlueF,
            navigationIconContentColor = AppColors.BlueF,
            titleContentColor = AppColors.BlueF,
            actionIconContentColor = AppColors.BlueF
        )
    )
}

@Composable
fun ArticleItem(
    article: Article,
    onLongPress: () -> Unit
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgrounColorCard = UtilColorApp.backgroundCardItem(isSystemInDarkTheme)
    val colorText = UtilColorApp.getTextColor(isSystemInDarkTheme)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress()
                    })
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgrounColorCard)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(0.75f)
            ) {
                Text(
                    modifier = Modifier
                        .background(AppColors.Red, RoundedCornerShape(7.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                    text = article.local_code,
                    style = MaterialTheme.typography.titleSmall,
                    color = AppColors.White
                )

                Text(
                    article.name.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorText
                )
                Text(
                    text = stringResource(
                        R.string.pvp_label,
                        article.sale_price
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorText
                )
                if (article.quantity == 0) {
                    Text(
                        modifier = Modifier
                            .background(color = AppColors.Red, RoundedCornerShape(7.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp),
                        text = "Sin Stock",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppColors.White
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.quantity_label,
                            article.quantity
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = colorText
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notfoundimage),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun FloatingActionButtonMain(
    isFabExpanded: Boolean,
    navController: NavController,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        if (isFabExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(end = 16.dp, bottom = 72.dp)
        ) {
            AnimatedVisibility(
                visible = isFabExpanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Venta", modifier = Modifier.padding(end = 8.dp), color = AppColors.White)
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(SalesScreens.SaleScreen.name)
                        },
                        containerColor = AppColors.BlueF,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddShoppingCart,
                            contentDescription = "Back",
                            tint = AppColors.White
                        )
                    }
                }

            }
        }
        FloatingActionButton(
            onClick = {
                onClick()
            },
            containerColor = AppColors.BlueF,
        ) {
            Icon(
                imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.MoreVert,
                contentDescription = null,
                tint = AppColors.White
            )
        }
    }
}


/**
 * Maneja el resultado obtener articulos.
 */
fun handleGetListArticlesResponse(
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
