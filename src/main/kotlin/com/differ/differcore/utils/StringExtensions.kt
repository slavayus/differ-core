package com.differ.differcore.utils

fun String.isInt() = try {
    toInt()
    true
} catch (ex: NumberFormatException) {
    false
}