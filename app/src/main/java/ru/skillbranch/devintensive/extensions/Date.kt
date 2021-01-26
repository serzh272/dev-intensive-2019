package ru.skillbranch.devintensive.extensions

import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*

const val SECOND =1000L
const val MINUTE = 60* SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern:String = "HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}
fun Date.add(value:Int, units: TimeUnits):Date{
    var time = this.time
    time += when(units){
        TimeUnits.SECOND-> value * SECOND
        TimeUnits.MINUTE-> value * MINUTE
        TimeUnits.HOUR-> value * HOUR
        TimeUnits.DAY-> value * DAY
    }
    this.time = time
    return this
}
enum class TimeUnits{
    SECOND{
          override fun plural(units:Long):String{
              timeNames = listOf("секунду", "секунды", "секунд")
              return "$units " + if (units < 10 || units >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
          }
          },
    MINUTE{
        override fun plural(units:Long):String{
            timeNames = listOf("минуту", "минуты", "минут")
            return "$units " + if (units < 10 || units >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
        }
          },
    HOUR{
        override fun plural(units:Long):String{
            timeNames = listOf("час", "часа", "часов")
            return "$units " + if (units < 10 || units >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
        }
        },
    DAY{
        override fun plural(units:Long):String{
            timeNames = listOf("день", "дня", "дней")
            return "$units " + if (units < 10 || units >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
        }
    };
    var timeNames: List<String> = listOf()
    abstract fun plural(units:Long):String
}




fun Date.humanizeDiff(date:Date = Date()): String {
    val interval = date.time - this.time
    val minutes = abs(interval/ MINUTE)
    val hours = abs(interval/ HOUR)
    val days = abs(interval/ DAY)
    val exprBefore = "через %"
    val exprAfter = "% назад"
    val rez = when (abs(interval)){
        in 0L until SECOND -> if(interval < 0) "сейчас" else "только что"
        in SECOND until 45*SECOND -> "несколько секунд"
        in 45*SECOND until 75*SECOND -> "минуту"
        in 75*SECOND until 45*MINUTE -> TimeUnits.MINUTE.plural(minutes)
        in 45*MINUTE until 75* MINUTE -> "час"
        in 75*MINUTE until 22* HOUR -> TimeUnits.HOUR.plural(hours)
        in 22*HOUR until 26* HOUR -> "день"
        in 26*HOUR until DAY*360 -> TimeUnits.DAY.plural(days)
        else -> if(interval < 0) "более чем через год" else "более года назад"
    }
    return when (rez){
        "сейчас", "только что", "более чем через год", "более года назад" -> rez
        else -> (if(interval < 0) exprBefore else exprAfter).replace("%", rez)
    }
}


fun Date.shortFormat(): String? {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date:Date): Boolean{
    val day1 = this.time/ DAY
    val day2 = date.time/ DAY
    return day1 == day2
}