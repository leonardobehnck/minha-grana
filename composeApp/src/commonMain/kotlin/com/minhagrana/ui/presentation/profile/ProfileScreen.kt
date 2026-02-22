package com.minhagrana.ui.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.User
import com.minhagrana.models.profile.ProfileInteraction
import com.minhagrana.models.profile.ProfileViewModel
import com.minhagrana.models.profile.ProfileViewState
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.Dialog
import com.minhagrana.ui.components.Error
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.InputText
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.components.ProgressBar
import com.minhagrana.ui.components.noRippleClickable
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    navigateUp: () -> Unit,
    onSaveProfileSelected: () -> Unit,
    onDeleteAccountSelected: () -> Unit,
    viewModel: ProfileViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.interact(ProfileInteraction.OnScreenOpened)
    }

    when (val currentState = state) {
        is ProfileViewState.Idle,
        is ProfileViewState.Loading,
        -> {
            ProgressBar()
        }

        is ProfileViewState.Success -> {
            ProfileContent(
                user = currentState.user,
                navigateUp = navigateUp,
                onSaveProfileSelected = onSaveProfileSelected,
                onConfirmDeleteAccount = { viewModel.interact(ProfileInteraction.OnDeleteAccount) },
                onUpdateName = { name -> viewModel.interact(ProfileInteraction.OnUpdateName(name)) },
            )
        }

        is ProfileViewState.ProfileUpdated -> {
            LaunchedEffect(Unit) {
                onSaveProfileSelected()
            }
        }

        is ProfileViewState.Error -> {
            Error(message = currentState.message)
        }

        is ProfileViewState.AccountDeleted -> {
            LaunchedEffect(Unit) {
                onDeleteAccountSelected()
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    navigateUp: () -> Unit,
    onSaveProfileSelected: () -> Unit,
    onConfirmDeleteAccount: () -> Unit,
    onUpdateName: (String) -> Unit,
) {
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var name by remember(user) { mutableStateOf(TextFieldValue(user.name)) }

    LaunchedEffect(user) {
        name = TextFieldValue(user.name)
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            AppBar(
                navigateUp = navigateUp,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()),
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier =
                        Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                            .noRippleClickable(onClick = { showDeleteAccountDialog = true }),
                    text = "Excluir conta",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            PrimaryButton(
                modifier = Modifier.padding(top = 56.dp),
                title = "Salvar",
                enabled = name.text.isNotEmpty(),
                onClick = {
                    onUpdateName(name.text)
                    onSaveProfileSelected()
                },
            )

            Dialog(
                title = "Deletar conta",
                subtitle = "Tem certeza que deseja excluir sua conta?",
                description = "Todos seus dados serão perdidos.",
                actionButtonText = "Excluir",
                dismissButtonText = "Cancelar",
                onConfirmSelected = {
                    showDeleteAccountDialog = false
                    onConfirmDeleteAccount()
                },
                showBottomSheet = showDeleteAccountDialog,
                onDismiss = { showDeleteAccountDialog = false },
            )
        }
    }
}
