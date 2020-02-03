package com.differ.differcommit.naming.generator

interface VersionGenerator {
    fun generateVersionName(dversionsHomeDir: String): String
}