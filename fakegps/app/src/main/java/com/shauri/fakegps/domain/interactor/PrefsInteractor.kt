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

    fun setAccuracy(accuracy: Int) {
        storage.accuracy = accuracy
    }

    fun getAccuracy() = storage.accuracy

    fun setInterval(interval: Int) {
        storage.interval = interval
    }

    fun getInterval() = storage.interval

    fun isHms() = storage.hms

    fun setHms(mock: Boolean) {
        storage.hms = mock
    }

    fun isGms() = storage.gms

    fun setGms(mock: Boolean) {
        storage.gms = mock
    }
}