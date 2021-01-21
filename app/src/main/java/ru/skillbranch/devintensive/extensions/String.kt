package ru.skillbranch.devintensive.extensions

fun String.truncate(numSym:Int = 16):String{
    return if (this.trimEnd().length >numSym) this.trimStart().subSequence(0, numSym).toString().trimEnd() +"..." else this.trim()
}

fun String.stripHtml():String{
    val regExp = Regex(">([^<].+)<\\/")
    val rez = regExp.find(this)?.groupValues?.get(1)
    return (if (rez.isNullOrEmpty()) this else rez).replace(Regex("&#?\\w+;|[\'\"]*"), "").replace(Regex("\\s{2,}"), " ")
}
