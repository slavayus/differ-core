package com.differ.differcommit.generator

class TimestampVersionGenerator : VersionGenerator {
    override fun generateVersionName(dversionsHomeDir: String) = System.currentTimeMillis().toString()
}