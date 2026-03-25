package com.minhagrana.entities

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import com.minhagrana.ui.theme.categoryBaby
import com.minhagrana.ui.theme.categoryHealth
import com.minhagrana.ui.theme.categoryIncome
import com.minhagrana.ui.theme.categoryPet
import com.minhagrana.ui.theme.categoryTransport
import com.minhagrana.ui.theme.gray
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: Int = 0,
    var name: String = "Geral",
    @Contextual
    var color: Color = gray,
)

fun getDefaultColor(name: String): Color =
    when (name) {
        "Salário" -> categoryIncome
        "Transporte" -> categoryTransport
        "Pet" -> categoryPet
        "Pets" -> categoryPet
        "Filhos" -> categoryBaby
        "Saúde" -> categoryHealth
        else -> gray
    }

val CategorySaver: Saver<Category, Any> =
    listSaver(
        save = { listOf(it.id, it.name, it.color.value.toLong()) },
        restore = {
            Category(
                id = it[0] as Int,
                name = it[1] as String,
                color = Color(value = (it[2] as Long).toULong()),
            )
        },
    )

val NullableCategorySaver: Saver<Category?, Any> =
    listSaver(
        save = { category ->
            category?.let { listOf(it.id, it.name, it.color.value.toLong()) } ?: emptyList()
        },
        restore = { list ->
            if (list.isEmpty()) {
                null
            } else {
                Category(
                    id = list[0] as Int,
                    name = list[1] as String,
                    color = Color(value = (list[2] as Long).toULong()),
                )
            }
        },
    )
