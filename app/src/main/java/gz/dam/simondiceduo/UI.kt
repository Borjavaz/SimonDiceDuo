package gz.dam.simondiceduo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SimonDiceUI(viewModel: VM = viewModel()) {
    val context = LocalContext.current
    val soundPlayer = remember { SoundPlayer.getInstance(context) }

    // Estados del juego
    val gameState by viewModel.gameState.collectAsState()
    val ronda by viewModel.ronda.collectAsState()
    val record by viewModel.record.collectAsState()
    val text by viewModel.text.collectAsState()
    val colorActivo by viewModel.colorActivo.collectAsState()
    val botonesBrillantes by viewModel.botonesBrillantes.collectAsState()
    val sonidoEvent by viewModel.sonidoEvent.collectAsState()

    // Efecto para manejar sonidos
    LaunchedEffect(sonidoEvent) {
        when (val event = sonidoEvent) {
            is SonidoEvent.ColorSound -> {
                soundPlayer.playColorSound(event.colorInt)
            }
            is SonidoEvent.Error -> {
                soundPlayer.playErrorSound()
            }
            is SonidoEvent.Victory -> {
                soundPlayer.playVictorySound()
            }
            null -> {
                // No hacer nada
            }
        }
        // Limpiar el evento después de procesarlo
        viewModel.clearSoundEvent()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderInfo(ronda, record, text, gameState)
        BotonesColores(viewModel, colorActivo, botonesBrillantes, gameState)
        BotonControl(viewModel, gameState)
    }
}

