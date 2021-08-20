package com.geekstudio.entity

data class NotificationTime(
    val hour: Int,
    val minute: Int,
    val second: Int,
) {

    fun toStringHourMinute(): String {
        return "${hour}시간 ${minute}분"
    }

    fun toStringHourMinuteSecond(): String {
        return "${hour}시간 ${minute}분 ${second}초"
    }

    fun toStringMinute(): String {
        return "${minute}분"
    }

    fun toSecond(): Int = ((TimeUnit.HOUR.value * hour) + (TimeUnit.MINUTE.value * minute) + second)

    fun toMinute(): Int = ((TimeUnit.HOUR.value * hour) + (TimeUnit.MINUTE.value * minute) + second) / TimeUnit.MINUTE.value

    override fun toString(): String {
        return "$hour:$minute:$second"
    }

    companion object{
        private val DEFAULT_TIME = NotificationTime(0, 25, 0)

        fun covertNotification(time: Int): NotificationTime {
            var timeMap = HashMap<TimeUnit,Int>()
            var tempCount = time

            timeUnitList.forEach { unit ->
                if (unit.value <= tempCount) {
                    timeMap[unit] = tempCount / unit.value
                    tempCount %= unit.value
                }
            }

            return NotificationTime(
                timeMap[TimeUnit.HOUR]?:0,
                timeMap[TimeUnit.MINUTE]?:0,
                timeMap[TimeUnit.SECOND]?:0,
            )
        }


        /**
         * Notification Time 형식 -> 시간:분:초
         */
        fun covertNotification(time:String): NotificationTime {
            if(time.isNullOrEmpty())
                return DEFAULT_TIME

            val data = time.split(":")

            return if (data.size == timeUnitList.size) {
                NotificationTime(data[0].toInt(), data[1].toInt(), data[2].toInt())
            } else {
                DEFAULT_TIME
            }
        }
    }
}