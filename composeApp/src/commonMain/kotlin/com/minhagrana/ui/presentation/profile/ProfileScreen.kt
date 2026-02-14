package com.minhagrana.ui.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.User
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.Dialog
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.InputText
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.components.noRippleClickable

@Composable
fun ProfileScreen(
    navigateUp: () -> Unit = {},
    onSaveProfileSelected: () -> Unit = {},
    onDeleteAccountSelected: () -> Unit = {},
) {
    var showBottomSheetDeleteAccount by remember { mutableStateOf(false) }

    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("Leo"),
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondaryContainer),
    ) {
        AppBar(
            title = "Minha conta",
            navigateUp = { navigateUp() },
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
        ) {
            Header1(
                title = "Editar conta",
            )
            InputText(
                title = "Nome",
                textFieldValue = name,
                onValueChange = { name = it },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
            )
        }
        Text(
            modifier =
                Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
                    .noRippleClickable { showBottomSheetDeleteAccount = true },
            text = "Excluir conta",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
        )

        PrimaryButton(
            title = "Editar conta",
            enabled = name.text.isNotEmpty(),
            onClick = onSaveProfileSelected,
        )
    }
    Dialog(
        title = "Deletar conta",
        subtitle = "Tem certeza que deseja excluir sua conta?",
        description = "Todos seus dados serão perdidos.",
        actionButtonText = "Excluir",
        dismissButtonText = "Cancelar",
        onConfirmSelected = onDeleteAccountSelected,
        showBottomSheet = showBottomSheetDeleteAccount,
        onDismiss = { showBottomSheetDeleteAccount = false },
    )
}