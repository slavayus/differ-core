package com.differ.differcommit.generator

import com.differ.differcommit.utils.isInt
import java.io.File
import java.util.*

class IncrementLastFileProvider : LastFileProvider {
    override fun provideLastFile(dversionsHomeDir: String): String =
        Arrays.stream(File(dversionsHomeDir).listFiles() ?: emptyArray())
            .map { it.nameWithoutExtension }
            .filter { it.isInt() }
            .map { it.toInt() }
            .max(Integer::compare)
            .orElse(-1)
            .toString()
}