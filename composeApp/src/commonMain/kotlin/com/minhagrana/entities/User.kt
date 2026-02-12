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
    @SerialName("id")
    val id: Int = -1,
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("password")
    val password: String = "",
    @SerialName("balanceVisibility")
    val balanceVisibility: Boolean = true,
)
