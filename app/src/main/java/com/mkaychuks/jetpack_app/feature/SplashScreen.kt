package com.mkaychuks.jetpack_app.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mkaychuks.jetpack_app.common.components.CustomButtonWidget
import com.mkaychuks.jetpack_app.feature.auth.login.SignInScreen
import com.mkaychuks.jetpack_app.feature.auth.signup.SignUpScreen
import com.mkaychuks.jetpack_app.ui.theme.PurpleBackground
import com.mkaychuks.jetpack_app.ui.theme.WhiteColor
import com.mkaychuks.jetpack_app.ui.theme.fontFamily
import kotlinx.serialization.Serializable


@Serializable
object SplashScreen

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(horizontal = 24.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // the title
        Text(
            text = "Simplest Messaging App",
            fontFamily = fontFamily,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = WhiteColor,
            lineHeight = 40.sp
        )

        // the button
        Spacer(modifier = Modifier.height(16.dp))
        CustomButtonWidget(
            title = "Get started",
            onClick = {
                navController.navigate(SignUpScreen)
            },
            contentColor = PurpleBackground, containerColor = WhiteColor,
        )

        // Already a member
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = {
                navController.navigate(SignInScreen)
            }) {
                Text(
                    text = "Already a member? Sign in",
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}