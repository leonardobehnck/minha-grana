package com.minhagrana.ui.components

import androidx.compose.runtime.Composable
import com.minhagrana.entities.Category
import com.minhagrana.entities.CategoryStringKeys
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.category_children
import minhagrana.composeapp.generated.resources.category_general
import minhagrana.composeapp.generated.resources.category_health
import minhagrana.composeapp.generated.resources.category_pet
import minhagrana.composeapp.generated.resources.category_salary
import minhagrana.composeapp.generated.resources.category_transport
import org.jetbrains.compose.resources.stringResource

@Composable
fun Category.localizedName(): String =
    when (stringKey) {
        CategoryStringKeys.SALARY -> stringResource(Res.string.category_salary)
        CategoryStringKeys.TRANSPORT -> stringResource(Res.string.category_transport)
        CategoryStringKeys.HEALTH -> stringResource(Res.string.category_health)
        CategoryStringKeys.CHILDREN -> stringResource(Res.string.category_children)
        CategoryStringKeys.PET -> stringResource(Res.string.category_pet)
        CategoryStringKeys.GENERAL -> stringResource(Res.string.category_general)
        else -> name
    }
