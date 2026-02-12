package com.minhagrana.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YearWrapper(
    @SerialName("user")
    val year: Year,
)

@Serializable
data class Year(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("months")
    val months: List<Month>,
)
