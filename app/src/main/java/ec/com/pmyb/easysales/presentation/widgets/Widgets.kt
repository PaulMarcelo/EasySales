package ec.com.pmyb.easysales.presentation.widgets

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.presentation.utils.NumberVisualTransformation
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import ec.com.pmyb.easysales.ui.theme.AppColors

@Composable
fun ConfirmDialog(
    message: String,
    showDialog: Boolean,
    isSystemInDarkTheme: Boolean? = isSystemInDarkTheme(),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colorText = isSystemInDarkTheme?.let { UtilColorApp.getTextColor(it) }!!
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = stringResource(R.string.confirmation), color = colorText) },
            text = { Text(message, color = colorText) },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.CremaF_1,
                        contentColor = AppColors.White
                    )
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Gray5,
                        contentColor = AppColors.White
                    )
                ) {
                    Text(stringResource(R.string.no))
                }
            },
            containerColor = AppColors.BlueF,
        )
    }
}


@Composable
fun ProgressDialog(
    showDialog: Boolean
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(R.string.processing)) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            },
            confirmButton = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarAppWithBack(
    title: String,
    subtitle: String? = null,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior?,
    action: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    title,
                    overflow = TextOverflow.Ellipsis,
                    color = AppColors.White
                )
                subtitle?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = subtitle,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.White
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = AppColors.White
                )
            }
        },
        actions = {
            action()
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
fun InputTextApp(
    modifier: Modifier = Modifier,
    label: String,
    isRequire: Boolean = false,
    value: String,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    numberVisualTransformation: VisualTransformation,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = {
            if (isRequire) {
                RequiredLabel(text = label)
            } else {
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
            }
        },
        textStyle = TextStyle(fontSize = 11.sp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        onValueChange = { input ->
            onValueChange(input)
        },
        visualTransformation = numberVisualTransformation,
        colors = textFieldAppColors()
    )
}


@Composable
fun textFieldColorsAppDisable() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppColors.BlueF,
    unfocusedBorderColor = AppColors.BlueF,
    disabledBorderColor = AppColors.BlueF,
    disabledLabelColor = AppColors.BlueF,
)

@Composable
fun textFieldAppColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppColors.BlueF,
    focusedLabelColor = AppColors.BlueF,
    cursorColor = AppColors.BlueF,
)

@Composable
fun RequiredLabel(text: String) {
    Row {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = " *",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}