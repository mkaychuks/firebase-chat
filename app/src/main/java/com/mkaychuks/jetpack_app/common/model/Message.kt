package com.mkaychuks.jetpack_app.common.model

import com.google.firebase.Timestamp
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
data class Message(
    val senderID: String = "",
    val senderEmail: String = "",
    val receiverID: String = "",
    val message: String = "",

    @Contextual
    val timestamp: Timestamp = Timestamp.now(),
)
