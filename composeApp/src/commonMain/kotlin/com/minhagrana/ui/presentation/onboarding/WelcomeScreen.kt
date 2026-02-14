package com.minhagrana.ui.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
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
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.logo_small
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen(
    onUserCreated: () -> Unit,
    viewModel: OnboardingViewModel = koinInject(),
) {
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(state) {
        if (state is OnboardingViewState.Success) {
            onUserCreated()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        when (state) {
            is OnboardingViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is OnboardingViewState.Success -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
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
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 200.dp)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer),
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(48.dp),
                painter = painterResource(Res.drawable.logo_small),
                contentDescription = "Logo MinhaGrana",
                contentScale = ContentScale.Fit,
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Boas-vindas",
                style = MaterialTheme.typography.headlineMedium,
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
                title = "Nome",
                textFieldValue = name,
                onValueChange = onNameChange,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
            )
            PrimaryButton(
                modifier = Modifier.padding(bottom = 16.dp),
                enabled = name.text.isNotBlank(),
                title = "Criar conta",
                onClick = { onCreateAccount(name.text) },
            )
        }
    }
}
