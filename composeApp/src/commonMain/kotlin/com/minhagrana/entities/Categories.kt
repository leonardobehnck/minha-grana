package com.minhagrana.entities

import com.minhagrana.ui.theme.categoryBaby
import com.minhagrana.ui.theme.categoryHealth
import com.minhagrana.ui.theme.categoryIncome
import com.minhagrana.ui.theme.categoryPet
import com.minhagrana.ui.theme.categoryTransport
import com.minhagrana.ui.theme.gray
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
