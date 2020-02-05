package com.differ.differcommit.utils

import java.util.*

fun String.isInt() = toIntOrNull() != null

fun allIsNull(vararg any: Any?) = any.filterNotNull().isEmpty()

fun requireNotNull(any: Any?, error: () -> Throwable) = run {
    if (Objects.isNull(any)) {
        throw error.invoke()
    }
}