package ru.skillbranch.devintensive.extentions

fun String.truncate(numSym:Int = 16):String{
    return if (this.length >numSym) this.subSequence(0, numSym).toString().trimEnd() +"..." else this
}

fun String.stripHtml():String{
    val regExp = Regex(">(.*)<")
    val rez = regExp.find(this)?.groupValues?.get(1)
    return (if (rez.isNullOrEmpty()) this else rez).replace(Regex("[&\'\"]*"), "").replace(Regex("\\s{2,}"), " ")
}
