package com.differ.differcommit.utils

fun String.isInt() = try {
    toInt()
    true
} catch (ex: NumberFormatException) {
    false
}