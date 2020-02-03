package com.differ.differcommit.naming.generator

class TimestampVersionGenerator : VersionGenerator {
    override fun generateVersionName(dversionsHomeDir: String) = System.currentTimeMillis().toString()
}