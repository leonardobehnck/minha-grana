package com.minhagrana.entities

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import com.minhagrana.ui.theme.gray
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: Int = 0,
    var name: String = "Geral",
    @Contextual
    var color: Color = gray,
    var stringKey: String? = null,
)

val CategorySaver: Saver<Category, Any> =
    listSaver(
        save = { listOf(it.id, it.name, it.color.value.toLong(), it.stringKey ?: "") },
        restore = {
            Category(
                id = it[0] as Int,
                name = it[1] as String,
                color = Color(value = (it[2] as Long).toULong()),
                stringKey = (it.getOrNull(3) as? String)?.takeIf { s -> s.isNotEmpty() },
            )
        },
    )

val NullableCategorySaver: Saver<Category?, Any> =
    listSaver(
        save = { category ->
            category?.let {
                listOf(it.id, it.name, it.color.value.toLong(), it.stringKey ?: "")
            } ?: emptyList()
        },
        restore = { list ->
            if (list.isEmpty()) {
                null
            } else {
                Category(
                    id = list[0] as Int,
                    name = list[1] as String,
                    color = Color(value = (list[2] as Long).toULong()),
                    stringKey = (list.getOrNull(3) as? String)?.takeIf { s -> s.isNotEmpty() },
                )
            }
        },
    )
