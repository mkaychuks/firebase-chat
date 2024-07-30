package com.mkaychuks.jetpack_app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mkaychuks.jetpack_app.common.model.User
import com.mkaychuks.jetpack_app.feature.auth.login.SignInScreen
import com.mkaychuks.jetpack_app.feature.chat.ChatScreen
import com.mkaychuks.jetpack_app.ui.theme.PurpleBackground
import com.mkaychuks.jetpack_app.ui.theme.WhiteColor
import com.mkaychuks.jetpack_app.ui.theme.fontFamily
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    val viewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
    val uiState = viewModel.state.collectAsState().value
    val userList = viewModel.userList.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Available DMs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = fontFamily
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.signOutUser()
                        navController.navigate(SignInScreen) {
                            popUpTo(HomeScreen) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                    }
                }
            )
        }
    ) {
        if (uiState == HomeScreenState.Loading) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(WhiteColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteColor)
                    .padding(it)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                LazyColumn {
                    itemsIndexed(userList) { index: Int, user: User ->
                        UserTile(
                            user = user,
                            onClick = {
                                navController.navigate(
                                    ChatScreen(
                                        receiverUserID = user.uid,
                                        receiverUserFullName = user.fullName,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun UserTile(
    modifier: Modifier = Modifier,
    user: User,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // the avatar
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, PurpleBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${user.fullName[0]}",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // the name
            Text(
                text = user.fullName,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
        Divider(color = PurpleBackground, thickness = 0.5.dp)
    }
}