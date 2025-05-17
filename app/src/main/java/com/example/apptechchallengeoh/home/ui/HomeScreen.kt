import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptechchallengeoh.home.ui.CreateEventScreen
import com.example.apptechchallengeoh.ui.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Definir el NavController para los tabs
    val tabNavController = rememberNavController()
    // Estado para manejar el tab seleccionado
    val selectedTab = remember { mutableStateOf("eventList") }

    Scaffold(
        topBar = {  TitleBar("Pantalla Principal")},
        bottomBar = {
            NavigationBar {
                // Tab 1: Lista de eventos
                NavigationBarItem(
                    selected = selectedTab.value == "eventList", // El tab seleccionado se gestiona dinámicamente
                    onClick = {
                        selectedTab.value = "eventList"
                        tabNavController.navigate("eventList") {
                            // Evitar que el usuario pueda volver al historial de navegación
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    label = { Text("Lista de Eventos") },
                    icon = { Icon(Icons.Filled.List, contentDescription = "Lista de Eventos") }
                )

                // Tab 2: Crear evento
                NavigationBarItem(
                    selected = selectedTab.value == "createEvent", // El tab seleccionado se gestiona dinámicamente
                    onClick = {
                        selectedTab.value = "createEvent"
                        tabNavController.navigate("createEvent") {
                            // Evitar que el usuario pueda volver al historial de navegación
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    label = { Text("Crear Evento") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Crear Evento") }
                )
            }
        },
        content = { paddingValues ->
            // Configurar la navegación interna para los tabs
            NavHost(
                navController = tabNavController,
                startDestination = "eventList", // Página inicial de los tabs
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("eventList") {
                    EventListScreen()
                }
                composable("createEvent") {
                    CreateEventScreen(navController = tabNavController)
                }
            }
        }
    )
}

@Composable
fun EventListScreen() {
    Text("Lista de Eventos")
}


