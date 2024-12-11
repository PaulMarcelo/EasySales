package ec.com.pmyb.easysales.presentation.screens.login

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ec.com.pmyb.easysales.R
import ec.com.pmyb.easysales.presentation.navigation.SalesScreens
import ec.com.pmyb.easysales.ui.theme.AppColors
import kotlinx.coroutines.launch
import ec.com.pmyb.easysales.presentation.utils.UtilColorApp
import ec.com.pmyb.easysales.viewmodel.authentication.AuthenticactionViewModel
import ec.com.pmyb.easysales.viewmodel.user.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavHostController,
    authenticactionViewModel: AuthenticactionViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }
//    var username by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("yaguachi.marcelo@gmail.com") }
//    var password by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("fudisa.1234") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgrounColor = UtilColorApp.backgroundPrincipal(isSystemInDarkTheme)
    val colorText = UtilColorApp.getTextColor(isSystemInDarkTheme)

    val authenticationSuccess by authenticactionViewModel.authenticationSuccess.observeAsState()
    val errorAuthenticationMessage by authenticactionViewModel.errorAuthenticationMessage.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgrounColor)
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            value = username,
            label = { Text(text = stringResource(R.string.email)) },
            onValueChange = { username = it },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = colorText,
                unfocusedTextColor = colorText,
                focusedContainerColor = backgrounColor,
                unfocusedContainerColor = backgrounColor,
                cursorColor = colorText,
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            value = password,
            label = { Text(text = stringResource(R.string.password)) },
            onValueChange = { password = it },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = ""
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = colorText,
                unfocusedTextColor = colorText,
                focusedContainerColor = backgrounColor,
                unfocusedContainerColor = backgrounColor,
                cursorColor = colorText,
            )
        )
//        if (isAuthenticating) {
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.CenterHorizontally),
//                color = AppColors.BlueF,
//                trackColor = AppColors.CremaF
//            )
//        } else {
        Button(
            onClick = {
                authenticactionViewModel.signInWithEmailPassword(username, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(AppColors.BlueF)
        ) {
            Text(
                text = context.getString(R.string.accept), color = AppColors.White
            )
        }
//        }\

    }

    BackHandler {
        navController.popBackStack()
        (context as? Activity)?.finish()
    }

    handleAuthenticationResult(context,
        authenticationSuccess,
        errorAuthenticationMessage,
        success = {

            coroutineScope.launch {
                userViewModel.getUserByEmail(username,context)
                delay(3000)
                navController.navigate(SalesScreens.MainScreen.name)
            }
        },
        failure = {
        })

}

/**
 * Maneja el resultado de la autenticacion
 */
fun handleAuthenticationResult(
    context: Context,
    authenticationSuccess: Boolean?,
    errorAuthenticationMessage: String?,
    success: () -> Unit,
    failure: () -> Unit
) {
    errorAuthenticationMessage?.let { sms ->
        Toast.makeText(
            context,
            context.getString(R.string.ivalid_credential),
            Toast.LENGTH_SHORT
        ).show()
        failure()
    }
    authenticationSuccess?.let {
        if (it) {
            success()
        }
    }
}
