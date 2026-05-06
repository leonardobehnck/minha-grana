package com.minhagrana.ui.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.minhagrana.models.onboarding.OnboardingInteraction
import com.minhagrana.models.onboarding.OnboardingViewModel
import com.minhagrana.models.onboarding.OnboardingViewState
import com.minhagrana.ui.components.InputText
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.components.ProgressBar
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.create_account
import minhagrana.composeapp.generated.resources.enter_your_name
import minhagrana.composeapp.generated.resources.logo_content_description
import minhagrana.composeapp.generated.resources.logo_small
import minhagrana.composeapp.generated.resources.name
import minhagrana.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen(
    onUserCreated: () -> Unit,
    viewModel: OnboardingViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        when (state) {
            is OnboardingViewState.Loading -> {
                ProgressBar()
            }

            is OnboardingViewState.Success -> {
                LaunchedEffect(state) {
                    onUserCreated()
                }
            }

            else -> {
                WelcomeContent(
                    name = name,
                    onNameChange = { name = it },
                    errorMessage = (state as? OnboardingViewState.Error)?.message,
                    onCreateAccount = { viewModel.interact(OnboardingInteraction.OnCreateUser(it)) },
                    padding = padding,
                )
            }
        }
    }
}

@Composable
private fun WelcomeContent(
    name: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit,
    errorMessage: String?,
    onCreateAccount: (String) -> Unit,
    padding: PaddingValues,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(top = 175.dp)
                        .background(MaterialTheme.colorScheme.surface),
            ) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(horizontal = 16.dp),
                    painter = painterResource(Res.drawable.logo_small),
                    contentDescription = stringResource(Res.string.logo_content_description),
                    contentScale = ContentScale.Fit,
                )
            }
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = stringResource(Res.string.welcome),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = stringResource(Res.string.enter_your_name),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
                InputText(
                    title = stringResource(Res.string.name),
                    textFieldValue = name,
                    onValueChange = onNameChange,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            PrimaryButton(
                modifier = Modifier.padding(bottom = 16.dp),
                enabled = name.text.isNotBlank(),
                title = stringResource(Res.string.create_account),
                onClick = { onCreateAccount(name.text) },
            )
        }
    }
}
