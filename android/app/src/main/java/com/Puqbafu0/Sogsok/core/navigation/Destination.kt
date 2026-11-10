package com.Puqbafu0.Sogsok.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Levels : Screen
    data class Game(val levelIndex: Int, val attempt: Int) : Screen
    data class Result(val levelIndex: Int) : Screen
    data object Collection : Screen
    data object Progress : Screen
    data object Settings : Screen
}
