package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    hideKeyboard((currentFocus ?: View(this)))
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isKeyboardOpen():Boolean{
    var rect = Rect()
    var rectAct = Rect()
    View(this).getWindowVisibleDisplayFrame(rectAct)
    window.decorView.rootView.getWindowVisibleDisplayFrame(rect)
    return rectAct.bottom != rect.bottom
}

fun Activity.isKeyboardClosed():Boolean{
    return !isKeyboardOpen()
}