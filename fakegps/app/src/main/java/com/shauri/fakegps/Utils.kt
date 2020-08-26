package com.shauri.fakegps

import timber.log.Timber

private val APP_VER_REGEXP = Regex("\\d+(\\.\\d+){1,2}")

fun needUpgrade(currVersion: String?, newVersion: String?): Boolean {
    if (currVersion == null && newVersion == null
        || currVersion != null && !currVersion.matches(APP_VER_REGEXP)
        || newVersion != null && !newVersion.matches(APP_VER_REGEXP)
    ) {
        Timber.e("Incorrect version number current=%1\$s, new=%2\$s", currVersion, newVersion)
        return false
    }
    if (currVersion == null) {
        return true
    }
    if (newVersion == null) {
        return false
    }
    val o1t = currVersion.split("\\.".toRegex()).toTypedArray()
    val o2t = newVersion.split("\\.".toRegex()).toTypedArray()
    var res = compare(o1t[0], o2t[0])
    if (res == 0) { //first number is the same
        res = compare(o1t[1], o2t[1])
        if (res == 0) {    //second number is the same
            res = if (justTwoVersionNumbers(
                    o1t,
                    o2t
                )
            ) { // no more numbers - versions equals
                0
            } else if (o1t.size != o2t.size) {
                compareDifferentLengthVersions(o1t, o2t)
            } else {
                compare(o1t[2], o2t[2])
            }
        }
    }
    return res < 0
}

private fun compareDifferentLengthVersions(
    v1: Array<String>,
    v2: Array<String>
): Int {
    return v1.size - v2.size
}

private fun justTwoVersionNumbers(
    v1: Array<String>,
    v2: Array<String>
): Boolean {
    return v1.size == 2 && v2.size == 2
}

private fun compare(value1: String, value2: String): Int {
    val o10 = Integer.valueOf(value1)
    val o20 = Integer.valueOf(value2)
    return o10.compareTo(o20)
}