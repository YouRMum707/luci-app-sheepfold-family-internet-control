package app.sheepfold.android.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val mainTabs = listOf(
    "Главная",
    "Пользователи",
    "Белый список",
    "Чёрный список",
    "Расписание",
    "Настройки"
)

private enum class InternetState {
    Enabled,
    Disabled
}

private enum class DeviceStatus(
    val title: String,
    val color: Color
) {
    Allow("Белый список", Color(0xFF2E7D32)),
    Blocked("Чёрный список", Color(0xFFC62828)),
    Scheduled("По расписанию", Color(0xFF9A6700)),
    Restricted("Ограничено", Color(0xFF9A6700)),
    New("Новое", Color(0xFF1D4ED8))
}

private data class DeviceUi(
    val id: Int,
    val name: String,
    val ip: String,
    val mac: String,
    val group: String,
    val note: String,
    val status: DeviceStatus,
    val isAdmin: Boolean = false
)

private val demoDevices = listOf(
    DeviceUi(
        id = 1,
        name = "Телефон родителя",
        ip = "192.168.1.21",
        mac = "A4:5E:60:12:34:56",
        group = "Родители",
        note = "Всегда доступен",
        status = DeviceStatus.Allow,
        isAdmin = true
    ),
    DeviceUi(
        id = 2,
        name = "Планшет ребёнка",
        ip = "192.168.1.43",
        mac = "58:2F:40:AA:18:10",
        group = "Дети",
        note = "Учебный день, отбой 21:00",
        status = DeviceStatus.Scheduled
    ),
    DeviceUi(
        id = 3,
        name = "Телевизор в гостиной",
        ip = "192.168.1.77",
        mac = "F0:99:BF:70:22:09",
        group = "ТВ / медиа",
        note = "Разрешён после уроков",
        status = DeviceStatus.Restricted
    ),
    DeviceUi(
        id = 4,
        name = "Неизвестное устройство",
        ip = "192.168.1.98",
        mac = "DC:A6:32:8C:00:19",
        group = "Не настроено",
        note = "Найдено роутером",
        status = DeviceStatus.New
    ),
    DeviceUi(
        id = 5,
        name = "Старая игровая приставка",
        ip = "192.168.1.64",
        mac = "00:1F:16:CC:90:02",
        group = "Дети",
        note = "Заблокирована",
        status = DeviceStatus.Blocked
    )
)

@Composable
fun SheepfoldMainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var internetState by remember { mutableStateOf(InternetState.Enabled) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            mainTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title) }
                )
            }
        }

        when (selectedTab) {
            0 -> HomeControlScreen(
                internetState = internetState,
                onInternetStateChange = { internetState = it }
            )
            1 -> DevicesScreen(devices = demoDevices)
            2 -> DevicesScreen(
                devices = demoDevices.filter { device -> device.status == DeviceStatus.Allow },
                intro = "Эти устройства никогда не блокируются семейными правилами."
            )
            3 -> DevicesScreen(
                devices = demoDevices.filter { device -> device.status == DeviceStatus.Blocked },
                intro = "Эти устройства заблокированы всегда, пока родитель не изменит правило."
            )
            4 -> SchedulesScreen()
            5 -> SettingsScreen()
        }
    }
}

@Composable
private fun HomeControlScreen(
    internetState: InternetState,
    onInternetStateChange: (InternetState) -> Unit
) {
    ScreenSurface {
        SectionHeader(
            title = "Управление интернетом",
            body = "Команды будут отправляться на подключённый OpenWRT-роутер."
        )
        StatusCard(
            title = "Текущее состояние",
            body = if (internetState == InternetState.Enabled) {
                "Интернет включён"
            } else {
                "Интернет отключён для всех, кроме белого списка"
            },
            color = if (internetState == InternetState.Enabled) Color(0xFF2E7D32) else Color(0xFFC62828)
        )
        Button(
            onClick = { onInternetStateChange(InternetState.Enabled) },
            modifier = Modifier.fillMaxWidth(),
            enabled = internetState != InternetState.Enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFB7D9C2),
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "Интернет включён")
        }
        Button(
            onClick = { onInternetStateChange(InternetState.Disabled) },
            modifier = Modifier.fillMaxWidth(),
            enabled = internetState != InternetState.Disabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC62828),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE7AAA5),
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "Интернет отключён")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "Устройства",
                value = demoDevices.size.toString(),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Белый",
                value = demoDevices.count { device -> device.status == DeviceStatus.Allow }.toString(),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Чёрный",
                value = demoDevices.count { device -> device.status == DeviceStatus.Blocked }.toString(),
                modifier = Modifier.weight(1f)
            )
        }
        StatusCard(
            title = "Быстрые действия",
            body = "+15, +30, +1 час и доступ до отбоя будут подключены к API роутера следующим шагом.",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DevicesScreen(
    devices: List<DeviceUi>,
    intro: String = "Список берётся с роутера: аренды DHCP, ARP/neighbor-данные и постоянные аренды."
) {
    var selectedFilter by remember { mutableStateOf("Все") }
    val filters = listOf("Все", "Родители", "Дети", "Не настроено")
    val visibleDevices = if (selectedFilter == "Все") {
        devices
    } else {
        devices.filter { device -> device.group == selectedFilter }
    }

    ScreenSurface {
        SectionHeader(title = "Списки пользователей", body = intro)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(text = filter) }
                )
            }
        }
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color(0xFF2E7D32))
        ) {
            Text(text = "Добавить устройство", color = Color(0xFF14532D))
        }
        visibleDevices.forEach { device ->
            DeviceCard(device = device)
        }
    }
}

@Composable
private fun DeviceCard(device: DeviceUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFD1DDD8)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "#${device.id} ${if (device.isAdmin) "♛ " else ""}${device.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = device.note, style = MaterialTheme.typography.bodyMedium)
                }
                StatusPill(status = device.status)
            }
            Text(text = "IP: ${device.ip}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "MAC: ${device.mac}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Группа: ${device.group}", style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { }) {
                    Text(text = "Настроить")
                }
                OutlinedButton(onClick = { }) {
                    Text(text = "+30 мин")
                }
            }
        }
    }
}

@Composable
private fun SchedulesScreen() {
    ScreenSurface {
        SectionHeader(
            title = "Расписание",
            body = "Здесь будет управление правилами блокировки и разрешения по дням недели."
        )
        StatusCard(
            title = "Учебный день",
            body = "Пн-Пт: интернет разрешён после уроков, отбой в 21:00.",
            color = Color(0xFF9A6700)
        )
        StatusCard(
            title = "Быстрые разрешения",
            body = "+15 минут, +30 минут, +1 час, +2 часа, +3 часа, +5 часов, до конца суток и до отбоя.",
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Добавить правило")
        }
    }
}

@Composable
private fun SettingsScreen() {
    ScreenSurface {
        SectionHeader(
            title = "Настройки",
            body = "Основные параметры приложения и подключения к роутеру."
        )
        StatusCard(
            title = "Роутер",
            body = "Sheepfold API: http://192.168.1.1:5201",
            color = MaterialTheme.colorScheme.onSurface
        )
        StatusCard(
            title = "Аварийно-полезные сайты",
            body = "Редактируемый список доменов для ограниченного доступа. Широкие порталы вроде yandex.ru не добавляются по умолчанию.",
            color = MaterialTheme.colorScheme.onSurface
        )
        StatusCard(
            title = "Мессенджер",
            body = "VK по умолчанию, Telegram и MAX как отдельные варианты настройки на роутере.",
            color = MaterialTheme.colorScheme.onSurface
        )
        StatusCard(
            title = "Защита приложения",
            body = "Пароль или PIN рекомендуются. Биометрия включается только вручную.",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ScreenSurface(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content
    )
}

@Composable
private fun SectionHeader(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFD1DDD8)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    body: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFD1DDD8)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = body, style = MaterialTheme.typography.bodyMedium, color = color)
        }
    }
}

@Composable
private fun StatusPill(status: DeviceStatus) {
    Box(
        modifier = Modifier
            .background(
                color = status.color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.title,
            color = status.color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
