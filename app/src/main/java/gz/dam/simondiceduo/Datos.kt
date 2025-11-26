package gz.dam.simondiceduo


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import gz.dam.simondiceduo.ui.theme.SimonBlue
import gz.dam.simondiceduo.ui.theme.SimonGreen
import gz.dam.simondiceduo.ui.theme.SimonRed
import gz.dam.simondiceduo.ui.theme.SimonYellow

// IMPORTACIONES REQUERIDAS DE TU ARCHIVO Color.kt
import gz.dam.simondiceduo.ui.theme.SimonRedDark
import gz.dam.simondiceduo.ui.theme.SimonGreenDark
import gz.dam.simondiceduo.ui.theme.SimonBlueDark
import gz.dam.simondiceduo.ui.theme.SimonYellowDark

/**
 * Clase sellada que representa todos los estados posibles del juego
 */
sealed class GameState {
    object Inicio : GameState()
    object Preparando : GameState()
    object MostrandoSecuencia : GameState()
    object EsperandoJugador : GameState()
    object ProcesandoInput : GameState()
    object SecuenciaCorrecta : GameState()
    data class GameOver(val rondaAlcanzada: Int) : GameState()
}

/**
 * Clase que almacena los datos del juego
 */
object Datos {
    // Observers para notificar cambios
    private val observers = mutableListOf<(String) -> Unit>()

    fun addObserver(observer: (String) -> Unit) {
        observers.add(observer)
    }

    private fun notifyObservers(event: String) {
        observers.forEach { it(event) }
    }

    // Variables de Estado Reactivas
    private var _ronda by mutableStateOf(0)
    var ronda: Int
        get() = _ronda
        private set(value) {
            _ronda = value
            notifyObservers("RONDA_CHANGED")
        }

    private var _record by mutableStateOf(0)
    var record: Int
        get() = _record
        private set(value) {
            _record = value
            notifyObservers("RECORD_CHANGED")
        }

    private var _gameState by mutableStateOf<GameState>(GameState.Inicio)
    var gameState: GameState
        get() = _gameState
        private set(value) {
            _gameState = value
            notifyObservers("GAME_STATE_CHANGED")
        }

    var text by mutableStateOf("PRESIONA START")
    var mostrarSecuencia by mutableStateOf(false)
    var colorActivo by mutableStateOf(-1)
    var botonesBrillantes by mutableStateOf(false)
    var jugando by mutableStateOf(false)

    // Secuencias
    var secuencia = mutableListOf<Int>()
    var secuenciaUsuario = mutableListOf<Int>()

}