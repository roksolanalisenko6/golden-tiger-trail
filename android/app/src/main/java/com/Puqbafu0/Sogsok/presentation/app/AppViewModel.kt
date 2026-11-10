package com.Puqbafu0.Sogsok.presentation.app

import androidx.lifecycle.ViewModel
import com.Puqbafu0.Sogsok.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()
    private var attempts = 0

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun onSplashFinished() {
        if (_screen.value !is Screen.Splash) return
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun openRoute(target: Screen) {
        backStack.addLast(_screen.value)
        _screen.value = target
        syncBack()
    }

    fun openTrail(levelIndex: Int) {
        attempts += 1
        backStack.addLast(_screen.value)
        _screen.value = Screen.Game(levelIndex, attempts)
        syncBack()
    }

    fun replayTrail(levelIndex: Int) {
        attempts += 1
        _screen.value = Screen.Game(levelIndex, attempts)
        syncBack()
    }

    fun showResult(levelIndex: Int) {
        _screen.value = Screen.Result(levelIndex)
        syncBack()
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
