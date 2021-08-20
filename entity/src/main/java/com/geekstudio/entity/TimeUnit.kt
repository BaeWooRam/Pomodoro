package com.geekstudio.entity

enum class TimeUnit(val value: Int) {
    HOUR(3600),
    MINUTE(60),
    SECOND(1)
}

val timeUnitList = arrayOf(TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND)