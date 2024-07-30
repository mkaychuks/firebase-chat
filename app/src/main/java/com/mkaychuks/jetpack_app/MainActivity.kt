package com.mkaychuks.jetpack_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mkaychuks.jetpack_app.feature.SplashScreen
import com.mkaychuks.jetpack_app.feature.auth.login.SignInScreen
import com.mkaychuks.jetpack_app.feature.auth.signup.SignUpScreen
import com.mkaychuks.jetpack_app.feature.chat.ChatScreen
import com.mkaychuks.jetpack_app.feature.chat.ChatScreenMessages
import com.mkaychuks.jetpack_app.feature.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ChatApp()
        }
    }
}


@Composable
fun ChatApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashScreen) {
        composable<SplashScreen>(
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            }
        ) {
            SplashScreen(navController = navController)
        }
        composable<SignInScreen>(
        ) {
            SignInScreen(navController = navController)
        }
        composable<SignUpScreen>(
        ) {
            SignUpScreen(navController = navController)
        }
        composable<HomeScreen>(
        ) {
            HomeScreen(navController = navController)
        }
        composable<ChatScreen>(
        ) {
            val args = it.toRoute<ChatScreen>()
            ChatScreenMessages(
                receiverUserID = args.receiverUserID,
                receiverUserFullName = args.receiverUserFullName
            )
        }
    }
}