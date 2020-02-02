package com.differ.differcommit.generator

class IncrementVersionGenerator(private val lastFileProvider: LastFileProvider) : VersionGenerator {

    override fun generateVersionName(dversionsHomeDir: String): String =
        lastFileProvider.provideLastFile(dversionsHomeDir)
            .toInt()
            .inc()
            .toString()
            .padStart(4, '0')
}