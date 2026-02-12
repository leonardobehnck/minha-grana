package com.app.minhagrana.entities

import com.app.minhagrana.ui.theme.categoryBaby
import com.app.minhagrana.ui.theme.categoryHealth
import com.app.minhagrana.ui.theme.categoryIncome
import com.app.minhagrana.ui.theme.categoryPet
import com.app.minhagrana.ui.theme.categoryTransport
import com.app.minhagrana.ui.theme.gray
import kotlinx.serialization.Serializable

@Serializable
data class Categories(
    val categories: List<Category> =
        listOf(
            Category(
                name = "Salário",
                color = categoryIncome,
            ),
            Category(
                name = "Transporte",
                color = categoryTransport,
            ),
            Category(
                name = "Pet",
                color = categoryPet,
            ),
            Category(
                name = "Filhos",
                color = categoryBaby,
            ),
            Category(
                name = "Saúde",
                color = categoryHealth,
            ),
            Category(
                name = "Geral",
                color = gray,
            ),
        ),
)
