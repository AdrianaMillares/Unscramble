/**
 * Archivo que se encarga del manejo del juego
 * @author Paola Adriana Millares Forno - A01705674
 * @property Android Studio
 * @version 1.0 07/10/2021
 */

package com.example.android.unscramble.ui.game

// librerias importadas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragmento donde se juega, contiene toda la lógica del juego.
 * @param viewModel Contiene los metodos para correr el juego.
 * @param binding Objeto que da acceso a las vistas de los fragmentos del juego.
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: GameFragmentBinding

    /**
     * Crea la vista cuando se inicia la aplicación. Si el fragmento se recrea resive los mismos parametros
     * @param binding Objeto que da acceso a las vistas de los fragmentos del juego.
     * @return una instancia binding
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        return binding.root
    }

    /**
     * Genera las acciones del juego, submit word, skip word y cambiar de palabras
     * @param onSubmitWord función que registra la palabra escrita por el usuario
     * @param onSkipWord función que salta a la siguiente palabra
     * @param updateNextWordOnScreen funcion que muestra la siguiente palabra en pantalla
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
            R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    /**
     * Revisa que la palabra del usuario sea correcta y actualiza el puntaje de ser necesario.
     * Despliega la siguiente palabra por ordenar.
     * Al final de la ultima palabra muestra un cuadro de diálogo con el puntaje final.
     * @param playerWord palabra ingresada por el usuario
    */
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    /**
     * Salta a la siguiente palabra sin afectar el puntaje
     */
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }

    /**
     * Obtiene una palabra random de la lista previamente definida y mezcla las letras de esta
     * @param tempWord Guarda una palabra random de la lista de palabras predefinidas
     * @return la palabra generada de manera random
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    /**
     * Crea el cuadro de texto donde se muestra el puntaje final
     */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    /**
     * Reinicia la información del juego y actualiza la vista con nueva información
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

    /**
     * Termina el juego
     */
    private fun exitGame() {
        activity?.finish()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    /**
     * Despliega y resetea los campos de texto del error
     * @param error Muestra si existe un error
     */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /**
     * Despliega la siguiente palabra en pantalla
     */
    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }
}