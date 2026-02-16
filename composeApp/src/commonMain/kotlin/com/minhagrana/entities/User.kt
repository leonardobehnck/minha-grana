package com.minhagrana.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWrapper(
    @SerialName("user")
    val user: User,
)

@Serializable
data class User(
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("name")
    val name: String = "",
)
