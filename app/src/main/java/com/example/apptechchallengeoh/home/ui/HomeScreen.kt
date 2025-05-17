import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Definir el NavController para los tabs
    val tabNavController = rememberNavController()

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Pantalla Principal") }) },
        bottomBar = {
            NavigationBar {
                // Tab 1: Lista de eventos
                NavigationBarItem(
                    selected = true, // Aquí puedes gestionar el estado seleccionado dinámicamente
                    onClick = { tabNavController.navigate("eventList") },
                    label = { Text("Lista de Eventos") },
                    icon = { Icon(Icons.Filled.List, contentDescription = "Lista de Eventos") }
                )

                // Tab 2: Crear evento
                NavigationBarItem(
                    selected = false,
                    onClick = { tabNavController.navigate("createEvent") },
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
                    CreateEventScreen()
                }
            }
        }
    )
}

@Composable
fun EventListScreen() {
    Text("Lista de Eventos")
}

@Composable
fun CreateEventScreen() {
    Text("Formulario para Crear Evento")
}

