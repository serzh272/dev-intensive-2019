package ru.skillbranch.devintensive.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable

object Utils {
    fun parseFullName(fullName:String?):Pair<String?, String?>{
        val parts : List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return Pair(if(firstName.isNullOrEmpty()) null else firstName, if(lastName.isNullOrEmpty()) null else lastName)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val fn = if(firstName.isNullOrBlank()) null else firstName.elementAt(0).toUpperCase()
        val ln = if(lastName.isNullOrBlank()) null else lastName.elementAt(0).toUpperCase()
        return if (fn == null && ln == null){
            null
        }else if (fn == null || ln == null){
            if(fn == null){
                "$ln"
            }else{
                "$fn"
            }
        }else{
            "$fn$ln"
        }
    }

    fun transliteration(payload: String, divider:String = " "): String {
        val voc:Map<Char, String> = mapOf('а' to "a",
                                            'б' to "b",
                                            'в' to "v",
                                            'г' to "g",
                                            'д' to "d",
                                            'е' to "e",
                                            'ё' to "e",
                                            'ж' to "zh",
                                            'з' to "z",
                                            'и' to "i",
                                            'й' to "i",
                                            'к' to "k",
                                            'л' to "l",
                                            'м' to "m",
                                            'н' to "n",
                                            'о' to "o",
                                            'п' to "p",
                                            'р' to "r",
                                            'с' to "s",
                                            'т' to "t",
                                            'у' to "u",
                                            'ф' to "f",
                                            'х' to "h",
                                            'ц' to "c",
                                            'ч' to "ch",
                                            'ш' to "sh",
                                            'щ' to "sh'",
                                            'ъ' to "",
                                            'ы' to "i",
                                            'ь' to "",
                                            'э' to "e",
                                            'ю' to "yu",
                                            'я' to "ya",
                                            ' ' to divider)
        var rez = ""
        var curStr:String?
        for (ch:Char in payload.trim()){
            curStr = voc.get(ch.toLowerCase())
            if (curStr=="") continue
            if (ch.isUpperCase()){
                curStr = curStr?.replace(curStr[0], curStr.elementAt(0).toUpperCase())
            }
            rez += if (curStr.isNullOrEmpty()) ch else curStr
        }
        return if (rez == divider) "" else rez
    }
}


