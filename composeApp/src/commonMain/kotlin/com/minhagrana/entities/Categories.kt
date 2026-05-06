package com.minhagrana.entities

import com.minhagrana.ui.theme.categoryBaby
import com.minhagrana.ui.theme.categoryHealth
import com.minhagrana.ui.theme.categoryIncome
import com.minhagrana.ui.theme.categoryPet
import com.minhagrana.ui.theme.categoryTransport
import com.minhagrana.ui.theme.gray
import kotlinx.serialization.Serializable

object CategoryStringKeys {
    const val SALARY = "category_salary"
    const val TRANSPORT = "category_transport"
    const val HEALTH = "category_health"
    const val CHILDREN = "category_children"
    const val PET = "category_pet"
    const val GENERAL = "category_general"
}

@Serializable
data class Categories(
    val categories: List<Category> =
        listOf(
            Category(
                name = "Salário",
                color = categoryIncome,
                stringKey = CategoryStringKeys.SALARY,
            ),
            Category(
                name = "Transporte",
                color = categoryTransport,
                stringKey = CategoryStringKeys.TRANSPORT,
            ),
            Category(
                name = "Pet",
                color = categoryPet,
                stringKey = CategoryStringKeys.PET,
            ),
            Category(
                name = "Filhos",
                color = categoryBaby,
                stringKey = CategoryStringKeys.CHILDREN,
            ),
            Category(
                name = "Saúde",
                color = categoryHealth,
                stringKey = CategoryStringKeys.HEALTH,
            ),
            Category(
                name = "Geral",
                color = gray,
                stringKey = CategoryStringKeys.GENERAL,
            ),
        ),
)
