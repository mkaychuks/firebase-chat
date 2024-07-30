package com.mkaychuks.jetpack_app.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mkaychuks.jetpack_app.common.model.Message
import com.mkaychuks.jetpack_app.ui.theme.PurpleBackground
import com.mkaychuks.jetpack_app.ui.theme.WhiteColor
import com.mkaychuks.jetpack_app.ui.theme.fontFamily
import kotlinx.serialization.Serializable

@Serializable
data class ChatScreen(
    val receiverUserID: String,
    val receiverUserFullName: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenMessages(
    modifier: Modifier = Modifier,
    receiverUserID: String,
    receiverUserFullName: String
) {
    var messageText by remember {
        mutableStateOf("")
    }

    val chatViewModel: ChatViewModel = hiltViewModel<ChatViewModel>()
    val chatMessages = chatViewModel.chatMessages.collectAsState().value

    LaunchedEffect(key1 = true) {
        chatViewModel.getMessages(receiverUserID)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = receiverUserFullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = fontFamily
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(WhiteColor)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(chatMessages) { index: Int, item: Message ->
                    MessageTile(
                        isSender = item.senderID == chatViewModel.currentUserUid,
                        message = item
                    )
                }
            }

            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = {
                            messageText = it
                        },
                        textStyle = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 2,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        placeholder = {
                            Text(
                                text = "Message",
                                fontFamily = fontFamily
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            if (messageText.isNotEmpty()) {
                                chatViewModel.sendMessage(
                                    receiverID = receiverUserID,
                                    message = messageText,
                                )
                                messageText = ""
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send message",
                            tint = PurpleBackground,
                            modifier = Modifier.size(33.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MessageTile(
    modifier: Modifier = Modifier,
    isSender: Boolean,
    message: Message,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, end = if(isSender) 0.dp else 100.dp, start = if(isSender) 100.dp else 0.dp),
//            .padding(vertical = 4.dp, horizontal = 100.dp),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = if (isSender) PurpleBackground else Color(0xFFE6D5D5),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.message,
                fontFamily = fontFamily,
                color = if (isSender) WhiteColor else PurpleBackground,
            )
        }
    }
}