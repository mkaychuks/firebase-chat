package com.mkaychuks.jetpack_app.feature.auth.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mkaychuks.jetpack_app.common.components.CustomButtonWidget
import com.mkaychuks.jetpack_app.common.components.CustomTextFieldWidget
import com.mkaychuks.jetpack_app.feature.home.HomeScreen
import com.mkaychuks.jetpack_app.ui.theme.PurpleBackground
import com.mkaychuks.jetpack_app.ui.theme.WhiteColor
import com.mkaychuks.jetpack_app.ui.theme.fontFamily
import kotlinx.serialization.Serializable

@Serializable
object SignInScreen


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // validators
    var validateEmail: (String) -> String? =
        { if (it.isEmpty() || !it.contains("@")) "Enter a valid email" else null }
    var validatePassword: (String) -> String? =
        { if (it.isEmpty() && it.length < 6) "Password must be 6 chars long" else null }

    var passwordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    // viewmodel
    val viewModel: SignInViewModel = hiltViewModel<SignInViewModel>()
    val uiState = viewModel.state.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            SignInState.Error -> {
                Toast.makeText(context, "Sign In failed", Toast.LENGTH_SHORT).show()
            }

            SignInState.Success -> {
                navController.navigate(HomeScreen) {
                    popUpTo(SignInScreen) {
                        inclusive = true
                    }
                }
            }

            else -> {}
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteColor)
            .padding(horizontal = 16.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Log into your Account",
            fontFamily = fontFamily,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleBackground,
        )

        CustomTextFieldWidget(
            value = email,
            onValueChange = {
                email = it
            },
            label = "Email Address",
            leadingIcon = {
                Icon(
                    Icons.Outlined.Email,
                    contentDescription = null,
                    tint = PurpleBackground
                )
            },
            modifier = Modifier.padding(top = 10.dp),
            errorText = emailError ?: "",
            isError = emailError != null
        )

        CustomTextFieldWidget(
            value = password,
            onValueChange = {
                password = it
            },
            label = "Password",
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = PurpleBackground
                )
            },
            modifier = Modifier.padding(top = 10.dp),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            errorText = passwordError ?: ""
        )

        // the button
        Spacer(modifier = Modifier.height(44.dp))
        CustomButtonWidget(
            enabled = uiState.value != SignInState.Loading,
            title = if (uiState.value == SignInState.Loading) "Please wait..." else "Log in",
            contentColor = WhiteColor,
            containerColor = PurpleBackground,
            onClick = {
                emailError = validateEmail(email)
                passwordError = validatePassword(password)

                if (emailError == null && passwordError == null) {
                    viewModel.signIn(email, password)
                } else {
                    return@CustomButtonWidget
                }
            },
        )
    }
}