package com.minhagrana.ui.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.minhagrana.models.profile.ProfileInteraction
import com.minhagrana.models.profile.ProfileViewModel
import com.minhagrana.models.profile.ProfileViewState
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.Dialog
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.InputText
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.components.noRippleClickable
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    navigateUp: () -> Unit = {},
    onSaveProfileSelected: () -> Unit = {},
    onDeleteAccountSelected: () -> Unit = {},
    viewModel: ProfileViewModel = koinInject(),
) {
    var showBottomSheetDeleteAccount by remember { mutableStateOf(false) }
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.interact(ProfileInteraction.OnScreenOpened)
    }

    LaunchedEffect(state) {
        if (state is ProfileViewState.Success) {
            name = TextFieldValue((state as ProfileViewState.Success).user.name)
        }
        if (state is ProfileViewState.AccountDeleted) {
            onDeleteAccountSelected()
        }
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

        when (state) {
            is ProfileViewState.Idle,
            is ProfileViewState.Loading,
            -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is ProfileViewState.Success -> {
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                ) {
                    Header1(title = "Editar conta")
                    InputText(
                        title = "Nome",
                        textFieldValue = name,
                        onValueChange = { name = it },
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Text,
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
                    title = "Salvar",
                    enabled = name.text.isNotEmpty(),
                    onClick = {
                        viewModel.interact(ProfileInteraction.OnUpdateName(name.text))
                        onSaveProfileSelected()
                    },
                )
            }
            is ProfileViewState.Error -> {
                Text(
                    text = (state as ProfileViewState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp),
                )
            }
            is ProfileViewState.AccountDeleted -> { }
        }
    }
    Dialog(
        title = "Deletar conta",
        subtitle = "Tem certeza que deseja excluir sua conta?",
        description = "Todos seus dados serão perdidos.",
        actionButtonText = "Excluir",
        dismissButtonText = "Cancelar",
        onConfirmSelected = {
            viewModel.interact(ProfileInteraction.OnDeleteAccount)
            showBottomSheetDeleteAccount = false
        },
        showBottomSheet = showBottomSheetDeleteAccount,
        onDismiss = { showBottomSheetDeleteAccount = false },
    )
}
