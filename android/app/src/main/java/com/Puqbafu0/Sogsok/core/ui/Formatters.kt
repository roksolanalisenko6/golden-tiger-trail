package com.Puqbafu0.Sogsok.core.ui

import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.config.GameConfig
import com.Puqbafu0.Sogsok.domain.model.AnimalTrack

fun formatScore(value: Int): String {
    val digits = value.toString()
    val builder = StringBuilder()
    for (position in digits.indices) {
        if (position > 0 && (digits.length - position) % 3 == 0) {
            builder.append(',')
        }
        builder.append(digits[position])
    }
    return builder.toString()
}

fun relicSpriteRes(setId: Int): Int = when (setId.coerceIn(0, GameConfig.RELIC_SETS - 1)) {
    0 -> R.drawable.sprite_1
    1 -> R.drawable.sprite_2
    2 -> R.drawable.sprite_3
    3 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}

fun animalNameRes(animal: AnimalTrack): Int = when (animal) {
    AnimalTrack.TIGER -> R.string.animal_tiger
    AnimalTrack.SERPENT -> R.string.animal_serpent
    AnimalTrack.MONKEY -> R.string.animal_monkey
    AnimalTrack.HERON -> R.string.animal_heron
}

fun multiplierRes(step: Int): Int = when (step) {
    0 -> R.string.mult_one
    1 -> R.string.mult_half
    else -> R.string.mult_double
}
