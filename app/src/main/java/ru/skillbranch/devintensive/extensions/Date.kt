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
              return "$units " + if (units%100 < 10 || units%100 >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
          }
          },
    MINUTE{
        override fun plural(units:Long):String{
            timeNames = listOf("минуту", "минуты", "минут")
            return "$units " + if (units%100 < 10 || units%100 >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
        }
          },
    HOUR{
        override fun plural(units:Long):String{
            timeNames = listOf("час", "часа", "часов")
            return "$units " + if (units%100 < 10 || units%100 >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
        }
        },
    DAY{
        override fun plural(units:Long):String{
            timeNames = listOf("день", "дня", "дней")
            return "$units " + if (units%100 < 10 || units%100 >15) (if (units%10 in 2..4) timeNames[1] else if (units%10 == 1L) timeNames[0] else timeNames[2]) else timeNames[2]
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
        in 0L until SECOND+1 -> "только что"
        in SECOND+1 until 45*SECOND+1 -> "несколько секунд"
        in 45*SECOND+1 until 75*SECOND+1 -> "минуту"
        in 75*SECOND+1 until 45*MINUTE+1 -> TimeUnits.MINUTE.plural(minutes)
        in 45*MINUTE+1 until 75* MINUTE+1 -> "час"
        in 75*MINUTE+1 until 22* HOUR+1 -> TimeUnits.HOUR.plural(hours)
        in 22*HOUR+1 until 26* HOUR+1 -> "день"
        in 26*HOUR+1 until DAY*360+1 -> TimeUnits.DAY.plural(days)
        else -> if(interval < 0) "более чем через год" else "более года назад"
    }
    return when (rez){
        "сейчас", "только что", "более чем через год", "более года назад" -> rez
        else -> (if(interval < 0) exprBefore else exprAfter).replace("%", rez)
    }
}