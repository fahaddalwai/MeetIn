package com.isavit.meetin.core.util

import java.util.regex.Pattern

fun String.isValidEmail(): Boolean {
    var isValid = true
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    if (!matcher.matches()) {
        isValid = false
    }
    return isValid
}