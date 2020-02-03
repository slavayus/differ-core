package com.differ.differcommit.naming.provider

interface IntNameLastFileProvider : LastFileProvider {
    fun provideLastFile(dversionsHomeDir: String): Int
}