package com.differ.differcommit.naming.generator

import com.differ.differcommit.naming.provider.IntNameLastFileProvider

class IncrementVersionGenerator(private val lastFileProvider: IntNameLastFileProvider) :
    VersionGenerator {

    override fun generateVersionName(dversionsHomeDir: String): String =
        lastFileProvider.provideLastFile(dversionsHomeDir)
            .inc()
            .toString()
            .padStart(4, '0')
}