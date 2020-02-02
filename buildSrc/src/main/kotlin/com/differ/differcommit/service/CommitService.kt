package com.differ.differcommit.service

import com.differ.differcommit.generator.VersionGenerator

interface CommitService {
    fun processNewApiVersion(versionGenerator: VersionGenerator)
}