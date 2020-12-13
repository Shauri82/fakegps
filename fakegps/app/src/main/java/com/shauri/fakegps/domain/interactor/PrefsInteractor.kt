package com.shauri.fakegps.domain.interactor

import javax.inject.Inject

class PrefsInteractor @Inject constructor(val storage: PrefsStorage) {

    fun isMockMovement() = storage.isMockMovement

    fun setMockMovement(mock: Boolean) {
        storage.isMockMovement = mock
    }

    fun setRandomMovement(random: Boolean) {
        storage.isRandomMovement = random
    }

    fun isRandomMovement() = storage.isRandomMovement

    fun setArc(arc: Double) {
        storage.arc = arc
    }

    fun getArc() = storage.arc
}