package com.minhagrana.entities

import com.app.minhagrana.ui.currentMonth
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryWrapper(
    @SerialName("user")
    val entry: Entry,
)

@Serializable
data class Entry(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("name")
    val name: String = "Novo lançamento",
    @SerialName("value")
    val value: Double = 18850.0,
    @SerialName("date")
    val date: String = currentMonth,
    @SerialName("repeat")
    val repeat: Int = 1,
    @SerialName("type")
    val type: EntryType = EntryType.EXPENSE,
    @SerialName("category")
    val category: Category = Category(),
)

@Serializable
enum class EntryType {
    INCOME,
    EXPENSE,
}
