package com.mkaychuks.jetpack_app.common.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = ""
)
