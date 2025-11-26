
# Simón Dice - Juego de Memoria 🎮

## 🎯 Objetivo del Juego
Aplicación Android del clásico juego Simón Dice donde el jugador debe memorizar y repetir secuencias de colores y sonidos que aumentan en dificultad progresivamente. ¡Pon a prueba tu memoria visual y auditiva!

##  Características Implementadas
-  Secuencias de colores con feedback visual y auditivo
-  Sistema de rondas progresivas con aumento de dificultad
-  Gestión automática de récords
-  Efectos visuales para aciertos y errores
-  Interfaz moderna con Jetpack Compose
-  Sonidos distintivos para cada color
-  Estados de juego claramente definidos
-  Tests unitarios completos

## 🏗️ Arquitectura MVVM

### Model (Datos y Estados)
- **`Datos.kt`** - Objeto singleton que gestiona el estado del juego
- **`GameState.kt`** - Clase sellada que representa los 7 estados posibles del juego
- **`Colores`** - Enum con los colores del juego y sus propiedades
- **`SonidoEvent.kt`** - Eventos de sonido para comunicación unidireccional

### ViewModel (Lógica de Negocio)
- **`VM.kt`** - Gestiona la lógica completa del juego, estados y comunicación con la UI
- Utiliza `StateFlow` para estados reactivos
- Implementa corrutinas para operaciones asíncronas
- Maneja generación de secuencias, validación y transiciones de estado

### View (Interfaz de Usuario)
- **`UI.kt`** - Composable principal con componentes modulares
- **`MainActivity.kt`** - Actividad principal que configura Compose
- **`SoundPlayer.kt`** - Gestor de audio con patrón Singleton
- **Sistema de temas** - Tema personalizado con modo claro/oscuro

