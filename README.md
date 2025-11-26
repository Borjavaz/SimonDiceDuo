
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

## 🎮 Estados del Juego

| Estado | Descripción | UI |
|--------|-------------|-----|
| `Inicio` | Estado inicial, esperando inicio | "PRESIONA START" |
| `Preparando` | Preparando nueva partida | "PREPARADO..." |
| `MostrandoSecuencia` | Simón muestra la secuencia | "OBSERVA LA SECUENCIA" |
| `EsperandoJugador` | Turno del jugador | "TU TURNO - REPITE LA SECUENCIA" |
| `ProcesandoInput` | Procesando input del jugador | "PROCESANDO" |
| `SecuenciaCorrecta` | Secuencia completada correctamente | "¡BIEN! SIGUIENTE RONDA" |
| `GameOver` | Fin del juego | "GAME OVER - RONDA X" |

## 🎨 Sistema de Colores y Sonidos

| Color | Tono Musical | Código | Color Visual |
|-------|--------------|--------|--------------|
| **Verde** | Do (Alto) | 1 | 🟢 |
| **Rojo** | Mi (Medio-Alto) | 0 | 🔴 |
| **Azul** | Sol (Medio-Bajo) | 2 | 🔵 |
| **Amarillo** | Do' (Bajo) | 3 | 🟡 |

## 🚀 Instrucciones de Compilación

### Ejecución
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta en emulador o dispositivo físico (API 21+)

## 🎯 Flujo del Juego

1. **Inicio**: Presiona "START" para comenzar
2. **Observación**: Mira y escucha la secuencia de colores  
3. **Repetición**: Repite la secuencia en el mismo orden
4. **Progresión**: Cada ronda añade un color nuevo a la secuencia
5. **Game Over**: El juego termina al cometer un error
6. **Récord**: Se guarda automáticamente la mejor puntuación

## 📱 Funcionalidades Técnicas

### Reactividad
- Uso completo de `StateFlow` para todos los estados de UI
- Patrón de eventos para comunicación unidireccional
- Actualizaciones automáticas de la interfaz

### Corrutinas
- `viewModelScope` para operaciones asíncronas en el ViewModel
- `LaunchedEffect` para efectos secundarios en la UI
- `delay()` cancelable para temporizaciones

### Modularidad
- Componentes Compose reutilizables y testables
- Separación clara de responsabilidades
- Código limpio y mantenible

## 👥 Colaboración y Control de Versiones

### Estructura de Ramas
main (protegida)  
└── development  
├── feature_Borja # Lógica y estados  
└── featureManu # Interfaz y componentes  

Ejemplos de ejecucion del codigo:

<img width="406" height="849" alt="image" src="https://github.com/user-attachments/assets/3e791756-de80-4a58-8481-79252bf20ed7" />

<img width="406" height="849" alt="image" src="https://github.com/user-attachments/assets/773192ca-51dc-4996-be46-23a2e575bca6" />

