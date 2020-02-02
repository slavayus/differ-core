package com.differ.differcommit.generator

interface VersionGenerator {
    fun generateVersionName(dversionsHomeDir: String): String
}