package com.minhagrana.models.createcategory

sealed class CreateCategoryInteraction {
    data object OnScreenOpened : CreateCategoryInteraction()

    data class OnSaveClicked(
        val form: CreateCategoryFormState,
    ) : CreateCategoryInteraction()
}
