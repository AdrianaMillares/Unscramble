/**
 * Archivo que se encarga del manejo del juego
 * @author Paola Adriana Millares Forno - A01705674
 * @property Android Studio
 * @version 1.0 07/10/2021
 */
package com.example.android.unscramble.ui.game

// librerias importadas
import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * ViewModel que contiene la información del juego y los métodos para procesarla
 * @param score Acumula el puntaje del jugador
 * @param _currentWordCount Acumula la cantidad de palabras que se desplegaron
 * @param _currentScrambedWord Guarda la palabra mezclada
 * @param wordsList Lista de palabras utilizadas en el juego
 */
class GameViewModel : ViewModel(){
    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    /*
    * Updates currentWord and currentScrambledWord with the next word.
    */
    /**
     * Actualiza la palabra que se muestra en pantalla
     * @param currentWord Guarda una palabra random de la lista de palabras
     * @param tempWord Guarda la palabra random como un arreglo de caracteres para mezclarla
     */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    /**
     * Reinicia la información del juego
     * @param score Puntaje del juego
     * @param _currentWordCount Conteo de palabras mostradas
     * @param wordList Lista de palabras utilizadas
     */
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    /*
    * Increases the game score if the player's word is correct.
    */
    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    /**
     * Muestra si la palabra es correcta o no
     * @return true si la palabra es correcta
     * @return false si la palabra es incorrecta
     */
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /**
     * Muestra si el contador de palabrea es menor al máximo de palabras
     * @return true si el contador es menor al máximo de palabras
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
}
