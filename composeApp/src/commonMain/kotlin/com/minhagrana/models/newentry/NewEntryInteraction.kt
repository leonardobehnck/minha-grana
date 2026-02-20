package com.minhagrana.models.newentry

sealed class NewEntryInteraction {
    data object OnScreenOpened : NewEntryInteraction()

    data class OnSaveClicked(
        val form: NewEntryFormState,
    ) : NewEntryInteraction()
}
