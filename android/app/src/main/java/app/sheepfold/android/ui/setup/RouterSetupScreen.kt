package app.sheepfold.android.ui.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import app.sheepfold.android.R

@Composable
fun RouterSetupScreen() {
    var agreementAccepted by remember { mutableStateOf(false) }

    if (agreementAccepted) {
        PairingChoiceScreen()
    } else {
        AgreementScreen(onAccept = { agreementAccepted = true })
    }
}

@Composable
private fun AgreementScreen(onAccept: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sheepfold_logo),
            contentDescription = "Sheepfold",
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = "Sheepfold",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Перед настройкой примите пользовательское соглашение и условия обработки технических данных, необходимых для работы приложения.",
            style = MaterialTheme.typography.bodyLarge
        )
        TextButton(
            onClick = {
                uriHandler.openUri(
                    "https://github.com/kva4991/luci-app-sheepfold-family-internet-control/blob/main/docs/user-agreement.ru.md"
                )
            }
        ) {
            Text(text = "Открыть пользовательское соглашение")
        }
        Button(
            onClick = onAccept,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Принимаю")
        }
    }
}

@Composable
private fun PairingChoiceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Настройка подключения",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Выберите способ подключения к Sheepfold на OpenWRT-роутере.",
            style = MaterialTheme.typography.bodyLarge
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .widthIn(min = 148.dp)
            ) {
                Text(text = "QR")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .widthIn(min = 148.dp)
            ) {
                Text(text = "Авто настройка")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .widthIn(min = 148.dp)
            ) {
                Text(text = "Ручная настройка")
            }
        }
        SetupCard(
            title = "Следующий шаг",
            body = "После выбора способа приложение проверит подключение к домашнему Wi-Fi и настоящий MAC-адрес телефона."
        )
    }
}

@Composable
private fun SetupCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
