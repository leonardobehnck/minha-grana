package com.minhagrana.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthWrapper(
    @SerialName("user")
    val month: Month,
)

@Serializable
data class Month(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("income")
    val income: Double = 0.0,
    @SerialName("expense")
    val expense: Double = 0.0,
    @SerialName("balance")
    val balance: Double = 0.0,
    @SerialName("entries")
    val entries: List<Entry> = emptyList(),
)
