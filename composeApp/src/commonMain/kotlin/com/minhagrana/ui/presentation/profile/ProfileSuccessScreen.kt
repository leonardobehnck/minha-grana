package com.minhagrana.ui.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.components.PrimaryButton
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import minhagrana.composeapp.generated.resources.Res

@Composable
fun ProfileSuccessScreen(
    onContinue: () -> Unit,
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/success_animation.json").decodeToString(),
        )
    }
    val progress by animateLottieCompositionAsState(composition)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter =
                    rememberLottiePainter(
                        composition = composition,
                        progress = { progress },
                    ),
                contentDescription = "Profile updated animation",
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Perfil atualizado!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        PrimaryButton(
            title = "Voltar",
            enabled = true,
            onClick = onContinue,
        )
    }
}
