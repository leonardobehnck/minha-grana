package com.minhagrana.entities

import androidx.compose.ui.graphics.Color
import com.app.minhagrana.ui.theme.categoryBaby
import com.app.minhagrana.ui.theme.categoryHealth
import com.app.minhagrana.ui.theme.categoryIncome
import com.app.minhagrana.ui.theme.categoryPet
import com.app.minhagrana.ui.theme.categoryTransport
import com.app.minhagrana.ui.theme.gray
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
        "Pets" -> categoryPet
        "Filhos" -> categoryBaby
        "Saúde" -> categoryHealth
        else -> gray
    }
