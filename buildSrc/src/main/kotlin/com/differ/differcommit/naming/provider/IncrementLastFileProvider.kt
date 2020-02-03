package com.differ.differcommit.naming.provider

import com.differ.differcommit.utils.isInt
import java.io.File
import java.util.*

class IncrementLastFileProvider : IntNameLastFileProvider {
    override fun provideLastFile(dversionsHomeDir: String): Int =
        Arrays.stream(File(dversionsHomeDir).listFiles() ?: emptyArray())
            .map { it.nameWithoutExtension }
            .filter { it.isInt() }
            .map { it.toInt() }
            .max(Integer::compare)
            .orElse(-1)
}