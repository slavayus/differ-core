package com.differ.differcommit.generator

interface LastFileProvider {
    fun provideLastFile(dversionsHomeDir: String): String
}