package com.minhagrana.entities

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
