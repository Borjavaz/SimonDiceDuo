package gz.dam.simondiceduo


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel del juego Simon Dice
 */
class VM : ViewModel() {

    // Estados reactivos usando StateFlow
    private val _gameState = MutableStateFlow<GameState>(GameState.Inicio)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _ronda = MutableStateFlow(0)
    val ronda: StateFlow<Int> = _ronda.asStateFlow()

    private val _record = MutableStateFlow(0)
    val record: StateFlow<Int> = _record.asStateFlow()

    private val _text = MutableStateFlow("PRESIONA START")
    val text: StateFlow<String> = _text.asStateFlow()

    private val _colorActivo = MutableStateFlow(-1)
    val colorActivo: StateFlow<Int> = _colorActivo.asStateFlow()

    private val _botonesBrillantes = MutableStateFlow(false)
    val botonesBrillantes: StateFlow<Boolean> = _botonesBrillantes.asStateFlow()

    private val _sonidoEvent = MutableStateFlow<SonidoEvent?>(null)
    val sonidoEvent: StateFlow<SonidoEvent?> = _sonidoEvent.asStateFlow()

}